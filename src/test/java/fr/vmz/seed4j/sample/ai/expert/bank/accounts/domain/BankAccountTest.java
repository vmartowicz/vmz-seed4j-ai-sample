package fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain;

import static fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.AccountsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class BankAccountTest {

  @Nested
  class Opening {

    @Test
    void shouldOpenCurrentAccountWithOneMonthOfIncomeAsOverdraft() {
      var holder = adult("1500");

      var account = new AccountToOpen(AccountType.CURRENT, List.of(holder)).open(TODAY);

      assertThat(account.iban().isOurs()).isTrue();
      assertThat(account.type()).isEqualTo(AccountType.CURRENT);
      assertThat(account.holders()).containsExactly(holder.id());
      assertThat(account.authorizedOverdraft()).isEqualByComparingTo("1500");
      assertThat(account.balance()).isEqualByComparingTo("0");
      assertThat(account.isFrozen()).isFalse();
      assertThat(account.openedOn()).isEqualTo(TODAY);
    }

    @Test
    void shouldCapOverdraftAt2000() {
      assertThat(current("4000").authorizedOverdraft()).isEqualByComparingTo("2000");
    }

    @Test
    void shouldOpenJointAccount() {
      var account = new AccountToOpen(AccountType.CURRENT, List.of(adult("1000"), adult("3000"))).open(TODAY);

      assertThat(account.holders()).hasSize(2);
      assertThat(account.authorizedOverdraft()).isEqualByComparingTo("1000");
    }

    @Test
    void shouldNotOpenAccountForTheSameHolderTwice() {
      var holder = adult("1000");

      assertThatThrownBy(() -> new AccountToOpen(AccountType.CURRENT, List.of(holder, holder))).isExactlyInstanceOf(AccountNotAllowedException.class);
    }

    @Test
    void shouldNotOpenAccountWithThreeHolders() {
      assertThatThrownBy(() -> new AccountToOpen(AccountType.CURRENT, List.of(adult("1"), adult("1"), adult("1")))).isInstanceOf(AssertionException.class);
    }

    @Test
    void shouldOpenYouthAccountForMinorsOnly() {
      var youth = new AccountToOpen(AccountType.YOUTH, List.of(minor())).open(TODAY);

      assertThat(youth.authorizedOverdraft()).isEqualByComparingTo("0");
      assertThatThrownBy(() -> new AccountToOpen(AccountType.YOUTH, List.of(adult("1000")))).isExactlyInstanceOf(AccountNotAllowedException.class);
      assertThatThrownBy(() -> new AccountToOpen(AccountType.CURRENT, List.of(minor()))).isExactlyInstanceOf(AccountNotAllowedException.class);
      assertThatThrownBy(() -> new AccountToOpen(AccountType.YOUTH, List.of(minor(), minor()))).isExactlyInstanceOf(AccountNotAllowedException.class);
    }

    @Test
    void shouldOpenSavingsWithoutOverdraft() {
      assertThat(savings().authorizedOverdraft()).isEqualByComparingTo("0");
    }
  }

  @Nested
  class Movements {

    @Test
    void shouldDebitWithinAuthorizedOverdraft() {
      var account = current("1500");

      account.debit(euros("1500"), "Rent", NOW);

      assertThat(account.balance()).isEqualByComparingTo("-1500");
      assertThat(account.entries()).extracting(Entry::label).containsExactly("Rent");
    }

    @Test
    void shouldNotDebitBeyondAuthorizedOverdraft() {
      var account = current("1500");

      assertThatThrownBy(() -> account.debit(euros("1500.01"), "Rent", NOW)).isExactlyInstanceOf(InsufficientFundsException.class);
      assertThat(account.balance()).isEqualByComparingTo("0");
    }

    @Test
    void shouldNotDebitFrozenAccount() {
      var account = currentWith("100");
      account.freezeForSanctions();

      assertThatThrownBy(() -> account.debit(euros("10"), "Rent", NOW)).isExactlyInstanceOf(AccountFrozenException.class);
      assertThatThrownBy(() -> account.debitBeyondOverdraft(euros("10"), "Card", NOW)).isExactlyInstanceOf(AccountFrozenException.class);
    }

    @Test
    void shouldStayFrozenUntilEveryReasonIsLifted() {
      var account = currentWith("100");
      var holder = account.holders().getFirst();
      account.freezeForSanctions();
      account.freezeForReview(holder);
      assertThat(account.freezes()).containsExactlyInAnyOrder(FreezeReason.SANCTIONS, FreezeReason.REVIEW_OVERDUE);

      account.unfreezeForReview(holder);

      assertThat(account.isFrozen()).isTrue();
      assertThat(account.freezes()).containsExactly(FreezeReason.SANCTIONS);
    }

    @Test
    void shouldStayFrozenUntilEveryJointHolderIsReviewed() {
      var first = adult("1000");
      var second = adult("1000");
      var account = new AccountToOpen(AccountType.CURRENT, List.of(first, second)).open(TODAY);
      account.freezeForReview(first.id());
      account.freezeForReview(second.id());

      account.unfreezeForReview(first.id());
      assertThat(account.isFrozen()).isTrue();
      account.unfreezeForReview(second.id());

      assertThat(account.isFrozen()).isFalse();
    }

    @Test
    void shouldChargeBankEvenOnFrozenAccount() {
      var account = currentWith("100");
      account.freezeForReview(account.holders().getFirst());

      account.collectForBank(euros("60"), "Loan instalment", NOW);
      account.chargeFee(euros("20"), "Rejected direct debit", NOW);

      assertThat(account.balance()).isEqualByComparingTo("20");
      assertThatThrownBy(() -> account.collectForBank(euros("1600"), "Loan instalment", NOW)).isExactlyInstanceOf(InsufficientFundsException.class);
    }

    @Test
    void shouldCapLivretAAt22950() {
      var account = savings();
      account.credit(euros("22950"), "Deposit", NOW);

      assertThatThrownBy(() -> account.credit(euros("0.01"), "Deposit", NOW)).isExactlyInstanceOf(SavingsCeilingException.class);
    }

    @Test
    void shouldCreditOnlyPositiveAmounts() {
      assertThatThrownBy(() -> current("1000").credit(euros("0"), "Deposit", NOW)).isInstanceOf(AssertionException.class);
    }
  }

  @Nested
  class UnauthorizedOverdraft {

    @Test
    void shouldCharge8EurosPerOperationBeyondOverdraft() {
      var account = current("100");

      account.debitBeyondOverdraft(euros("50"), "Card", NOW);
      assertThat(account.wasInUnauthorizedOverdraftSince(TODAY.minusMonths(3))).isFalse();
      account.debitBeyondOverdraft(euros("60"), "Card", NOW);

      assertThat(account.balance()).isEqualByComparingTo("-118");
      assertThat(account.wasInUnauthorizedOverdraftSince(TODAY.minusMonths(3))).isTrue();
      assertThat(account.wasInUnauthorizedOverdraftSince(TODAY.plusDays(1))).isFalse();
    }

    @Test
    void shouldCapUnauthorizedOverdraftFeesAt80EurosAMonth() {
      var account = current("100");
      account.debitBeyondOverdraft(euros("100"), "Card", NOW);

      for (int operation = 0; operation < 11; operation++) {
        account.debitBeyondOverdraft(euros("1"), "Card", NOW);
      }

      assertThat(account.balance()).isEqualByComparingTo("-191");
      account.debitBeyondOverdraft(euros("1"), "Card", NOW.plusMonths(1));
      assertThat(account.balance()).isEqualByComparingTo("-200");
    }
  }

  @Nested
  class MonthlyClosing {

    @Test
    void shouldChargeAgiosAt16PercentAndMonthlyFee() {
      var account = current("2000");
      account.debit(euros("1000"), "Rent", NOW);
      for (int day = 0; day < 30; day++) {
        account.accrueInterest();
      }

      account.closeMonth(NOW.plusMonths(1), false);

      assertThat(account.balance()).isEqualByComparingTo("-1015.15");
    }

    @Test
    void shouldNotAccrueInterestOnPositiveBalance() {
      var account = currentWith("100");
      account.accrueInterest();

      account.closeMonth(NOW.plusMonths(1), true);

      assertThat(account.balance()).isEqualByComparingTo("100");
    }
  }
}
