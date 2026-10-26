package fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain;

import static fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.AccountsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class CardTest {

  private final BankAccount account = current("1500");

  @Test
  void shouldIssueCardOnCurrentAccount() {
    var card = Card.issue(account, CardType.CLASSIC);

    assertThat(card.id()).isNotNull();
    assertThat(card.account()).isEqualTo(account.iban());
    assertThat(card.type()).isEqualTo(CardType.CLASSIC);
    assertThat(card.status()).isEqualTo(CardStatus.ACTIVE);
  }

  @Test
  void shouldNotIssueCardOnSavings() {
    assertThatThrownBy(() -> Card.issue(savings(), CardType.CLASSIC)).isExactlyInstanceOf(CardNotAllowedException.class);
  }

  @Test
  void shouldNotIssueGoldCardOnYouthAccount() {
    var youth = new AccountToOpen(AccountType.YOUTH, List.of(minor())).open(TODAY);

    assertThat(Card.issue(youth, CardType.CLASSIC).type()).isEqualTo(CardType.CLASSIC);
    assertThatThrownBy(() -> Card.issue(youth, CardType.GOLD)).isExactlyInstanceOf(CardNotAllowedException.class);
  }

  @Nested
  class Payments {

    @Test
    void shouldLimitClassicPaymentsTo1500On30RollingDays() {
      var card = Card.issue(account, CardType.CLASSIC);
      card.recordPayment(euros("1000"), "FR", NOW.minusDays(29));

      assertThat(card.authorizePayment(euros("500"), "FR", NOW)).isEmpty();
      assertThat(card.authorizePayment(euros("500.01"), "FR", NOW)).contains(CardRefusal.LIMIT_REACHED);
      assertThat(card.authorizePayment(euros("1500"), "FR", NOW.plusDays(1))).isEmpty();
    }

    @Test
    void shouldAllowGoldPaymentsUpTo5000() {
      var card = Card.issue(account, CardType.GOLD);

      assertThat(card.authorizePayment(euros("5000"), "FR", NOW)).isEmpty();
      assertThat(card.authorizePayment(euros("5000.01"), "FR", NOW)).contains(CardRefusal.LIMIT_REACHED);
    }

    @Test
    void shouldBlockCardUsedInTwoCountriesWithinAnHour() {
      var card = Card.issue(account, CardType.CLASSIC);
      card.recordPayment(euros("10"), "FR", NOW);

      assertThat(card.authorizePayment(euros("10"), "ES", NOW.plusMinutes(59))).contains(CardRefusal.FRAUD_SUSPECTED);

      assertThat(card.status()).isEqualTo(CardStatus.BLOCKED);
      assertThat(card.authorizePayment(euros("10"), "FR", NOW.plusHours(2))).contains(CardRefusal.CARD_NOT_ACTIVE);
    }

    @Test
    void shouldAcceptOtherCountryAfterAnHour() {
      var card = Card.issue(account, CardType.CLASSIC);
      card.recordPayment(euros("10"), "FR", NOW);

      assertThat(card.authorizePayment(euros("10"), "ES", NOW.plusHours(1))).isEmpty();
      assertThat(card.authorizePayment(euros("10"), "FR", NOW.plusMinutes(5))).isEmpty();
    }
  }

  @Nested
  class Withdrawals {

    @Test
    void shouldLimitClassicWithdrawalsTo500On7RollingDays() {
      var card = Card.issue(account, CardType.CLASSIC);
      card.recordWithdrawal(euros("400"), NOW.minusDays(6));

      assertThat(card.authorizeWithdrawal(euros("100"), NOW)).isEmpty();
      assertThat(card.authorizeWithdrawal(euros("101"), NOW)).contains(CardRefusal.LIMIT_REACHED);
      assertThat(card.authorizeWithdrawal(euros("500"), NOW.plusDays(1))).isEmpty();
    }

    @Test
    void shouldAllowGoldWithdrawalsUpTo1000() {
      assertThat(Card.issue(account, CardType.GOLD).authorizeWithdrawal(euros("1000"), NOW)).isEmpty();
    }

    @Test
    void shouldNotWithdrawWithInactiveCard() {
      var card = Card.issue(account, CardType.CLASSIC);
      card.declareLostOrStolen();

      assertThat(card.authorizeWithdrawal(euros("10"), NOW)).contains(CardRefusal.CARD_NOT_ACTIVE);
    }
  }

  @Nested
  class Security {

    @Test
    void shouldBlockCardAfterThreeWrongPins() {
      var card = Card.issue(account, CardType.CLASSIC);

      card.wrongPin();
      card.wrongPin();
      assertThat(card.status()).isEqualTo(CardStatus.ACTIVE);
      card.wrongPin();

      assertThat(card.status()).isEqualTo(CardStatus.BLOCKED);
    }

    @Test
    void shouldOpposeLostCardForGood() {
      var card = Card.issue(account, CardType.CLASSIC);

      card.declareLostOrStolen();

      assertThat(card.status()).isEqualTo(CardStatus.OPPOSED);
      assertThatThrownBy(card::wrongPin).isExactlyInstanceOf(CardNotActiveException.class);
      assertThatThrownBy(card::declareLostOrStolen).isExactlyInstanceOf(CardNotActiveException.class);
    }
  }
}
