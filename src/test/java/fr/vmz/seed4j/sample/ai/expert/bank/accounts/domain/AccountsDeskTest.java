package fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain;

import static fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.AccountsFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class AccountsDeskTest {

  private final BankAccountsRepository accounts = mock(BankAccountsRepository.class, CALLS_REAL_METHODS);
  private final CardsRepository cards = mock(CardsRepository.class, CALLS_REAL_METHODS);
  private final CustomerDirectory customers = mock(CustomerDirectory.class);
  private final AccountsDesk desk = new AccountsDesk(accounts, cards, customers);

  @Nested
  class Opening {

    @Test
    void shouldOpenAccountForKnownHolders() {
      var holder = knownHolder(adult("1500"));

      var account = desk.open(AccountType.CURRENT, List.of(holder.id()), TODAY);

      assertThat(account.holders()).containsExactly(holder.id());
      verify(accounts).save(account);
    }

    @Test
    void shouldNotOpenAccountForUnknownHolder() {
      var unknown = CustomerId.newId();
      doReturn(Optional.empty()).when(customers).holder(unknown);

      assertThatThrownBy(() -> desk.open(AccountType.CURRENT, List.of(unknown), TODAY)).isExactlyInstanceOf(UnknownHolderException.class);
    }

    @Test
    void shouldDepositCash() {
      var account = known(current("1000"));

      desk.deposit(account.iban(), euros("300"), NOW);

      assertThat(account.balance()).isEqualByComparingTo("300");
      verify(accounts).save(account);
    }
  }

  @Nested
  class Freezes {

    @Test
    void shouldFreezeAndUnfreezeEveryAccountOfCustomer() {
      var customer = CustomerId.newId();
      var first = current("1000");
      var second = savings();
      doReturn(List.of(first, second)).when(accounts).ofHolder(customer);

      desk.freezeAccountsOf(customer);
      assertThat(first.isFrozen()).isTrue();
      assertThat(second.isFrozen()).isTrue();
      desk.unfreezeAccountsOf(customer);

      assertThat(first.isFrozen()).isFalse();
      verify(accounts, times(2)).save(first);
    }

    @Test
    void shouldFreezeOneAccount() {
      var account = known(current("1000"));

      desk.freezeForSanctions(account.iban());

      assertThat(account.freezes()).containsExactly(FreezeReason.SANCTIONS);
    }

    @Test
    void shouldNotFindUnknownAccount() {
      var unknown = Iban.generate();
      doReturn(Optional.empty()).when(accounts).get(unknown);

      assertThatThrownBy(() -> desk.account(unknown)).isExactlyInstanceOf(UnknownAccountException.class);
    }
  }

  @Nested
  class Lending {

    @Test
    void shouldFindCurrentAccountOfCustomer() {
      var customer = CustomerId.newId();
      var savings = savings();
      var current = current("1000");
      doReturn(List.of(savings, current)).when(accounts).ofHolder(customer);
      var saver = CustomerId.newId();
      doReturn(List.of(savings)).when(accounts).ofHolder(saver);

      assertThat(desk.currentAccountOf(customer)).contains(current.iban());
      assertThat(desk.currentAccountOf(saver)).isEmpty();
    }
  }

  @Nested
  class Cards {

    @Test
    void shouldIssueCard() {
      var account = known(current("1000"));

      var card = desk.issueCard(account.iban(), CardType.GOLD);

      verify(cards).save(card);
    }

    @Test
    void shouldDebitAuthorizedCardPayment() {
      var account = known(currentWith("100"));
      var card = known(Card.issue(account, CardType.CLASSIC));

      assertThat(desk.pay(card.id(), euros("150"), "FR", NOW).accepted()).isTrue();

      assertThat(account.balance()).isEqualByComparingTo("-50");
      assertThat(card.authorizePayment(euros("1350.01"), "FR", NOW)).contains(CardRefusal.LIMIT_REACHED);
      verify(cards).save(card);
      verify(accounts).save(account);
    }

    @Test
    void shouldRefuseCardPaymentOverLimit() {
      var account = known(currentWith("100"));
      var card = known(Card.issue(account, CardType.CLASSIC));

      assertThat(desk.pay(card.id(), euros("1501"), "FR", NOW).refusal()).contains(CardRefusal.LIMIT_REACHED);
      assertThat(account.balance()).isEqualByComparingTo("100");
    }

    @Test
    void shouldSaveCardBlockedForFraud() {
      var account = known(currentWith("100"));
      var card = known(Card.issue(account, CardType.CLASSIC));
      card.recordPayment(euros("10"), "FR", NOW);

      assertThat(desk.pay(card.id(), euros("10"), "IT", NOW.plusMinutes(10)).refusal()).contains(CardRefusal.FRAUD_SUSPECTED);
      verify(cards).save(card);
    }

    @Test
    void shouldRefuseCardPaymentOnFrozenAccount() {
      var account = known(currentWith("100"));
      var card = known(Card.issue(account, CardType.CLASSIC));
      account.freezeForSanctions();

      assertThat(desk.pay(card.id(), euros("10"), "FR", NOW).refusal()).contains(CardRefusal.ACCOUNT_FROZEN);
      assertThat(desk.withdraw(card.id(), euros("10"), NOW).refusal()).contains(CardRefusal.ACCOUNT_FROZEN);
    }

    @Test
    void shouldWithdrawCash() {
      var account = known(currentWith("100"));
      var card = known(Card.issue(account, CardType.CLASSIC));

      assertThat(desk.withdraw(card.id(), euros("60"), NOW).accepted()).isTrue();
      assertThat(desk.withdraw(card.id(), euros("441"), NOW).refusal()).contains(CardRefusal.LIMIT_REACHED);

      assertThat(account.balance()).isEqualByComparingTo("40");
    }

    @Test
    void shouldRecordWrongPinsAndOppositions() {
      var account = known(current("1000"));
      var card = known(Card.issue(account, CardType.CLASSIC));

      desk.wrongPin(card.id());
      desk.declareLostOrStolen(card.id());

      assertThat(desk.card(card.id()).status()).isEqualTo(CardStatus.OPPOSED);
      verify(cards, times(2)).save(card);
    }

    @Test
    void shouldNotFindUnknownCard() {
      var unknown = CardId.newId();
      doReturn(Optional.empty()).when(cards).get(unknown);

      assertThatThrownBy(() -> desk.card(unknown)).isExactlyInstanceOf(UnknownCardException.class);
    }
  }

  @Nested
  class Closing {

    @Test
    void shouldAccrueInterestDaily() {
      var account = current("1000");
      account.debit(euros("1000"), "Rent", NOW);
      doReturn(List.of(account)).when(accounts).all();

      desk.accrueInterest();
      desk.closeMonth(NOW.plusMonths(1));

      assertThat(account.balance()).isEqualByComparingTo("-1002.44");
      verify(accounts, times(2)).save(account);
    }

    @Test
    void shouldNotChargeMonthlyFeeWhenEveryHolderIsUnder25() {
      var young = new Holder(CustomerId.newId(), TODAY.minusYears(24), euros("1000"), false);
      var old = adult("1000");
      var youngOnly = withHolders(young);
      var joint = withHolders(young, old);
      doReturn(List.of(youngOnly, joint)).when(accounts).all();

      desk.closeMonth(NOW);

      assertThat(youngOnly.balance()).isEqualByComparingTo("100");
      assertThat(joint.balance()).isEqualByComparingTo("98");
    }

    @Test
    void shouldChargeMonthlyFeeWhenHolderIsUnknown() {
      var account = withHolders(adult("1000"));
      doReturn(Optional.empty()).when(customers).holder(account.holders().getFirst());
      doReturn(List.of(account)).when(accounts).all();

      desk.closeMonth(NOW);

      assertThat(account.balance()).isEqualByComparingTo("98");
    }

    private BankAccount withHolders(Holder... holders) {
      List.of(holders).forEach(AccountsDeskTest.this::knownHolder);
      var account = new AccountToOpen(AccountType.CURRENT, List.of(holders)).open(TODAY);
      account.credit(euros("100"), "Salary", NOW);

      return account;
    }
  }

  private Holder knownHolder(Holder holder) {
    doReturn(Optional.of(holder)).when(customers).holder(holder.id());

    return holder;
  }

  private BankAccount known(BankAccount account) {
    doReturn(Optional.of(account)).when(accounts).get(account.iban());

    return account;
  }

  private Card known(Card card) {
    doReturn(Optional.of(card)).when(cards).get(card.id());

    return card;
  }
}
