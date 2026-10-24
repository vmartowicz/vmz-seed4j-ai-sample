package fr.vmz.seed4j.sample.ai.simple.bank.domain;

import static fr.vmz.seed4j.sample.ai.simple.bank.domain.AccountsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.util.Optional;
import org.junit.jupiter.api.Test;

@UnitTest
class AccountTest {

  @Test
  void shouldOpenAccountWithZeroBalance() {
    var account = Account.open(jane(), NOW);

    assertThat(account.id()).isNotNull();
    assertThat(account.holder()).isEqualTo(jane());
    assertThat(account.openedAt()).isEqualTo(NOW);
    assertThat(account.balance()).isEqualTo(Balance.ZERO);
    assertThat(account.history()).isEmpty();
  }

  @Test
  void shouldReceiveTransfer() {
    var account = account();

    var operation = account.receiveTransfer(amount("100"), label("Salary"), NOW);

    assertThat(account.balance()).isEqualTo(balance("100"));
    assertThat(operation.type()).isEqualTo(OperationType.TRANSFER_RECEIVED);
    assertThat(operation.amount()).isEqualTo(amount("100"));
    assertThat(operation.label()).isEqualTo(label("Salary"));
    assertThat(operation.date()).isEqualTo(NOW);
    assertThat(operation.balanceAfter()).isEqualTo(balance("100"));
  }

  @Test
  void shouldMakePayment() {
    var account = accountWith("100");

    var operation = account.pay(amount("30.50"), Optional.empty(), NOW);

    assertThat(account.balance()).isEqualTo(balance("69.50"));
    assertThat(operation.type()).isEqualTo(OperationType.PAYMENT);
    assertThat(operation.label()).isEmpty();
    assertThat(operation.balanceAfter()).isEqualTo(balance("69.50"));
  }

  @Test
  void shouldPayWholeBalance() {
    var account = accountWith("100");

    account.pay(amount("100"), Optional.empty(), NOW);

    assertThat(account.balance()).isEqualTo(Balance.ZERO);
  }

  @Test
  void shouldNotPayMoreThanBalance() {
    var account = accountWith("100");

    assertThatThrownBy(() -> account.pay(amount("100.01"), Optional.empty(), NOW))
      .isExactlyInstanceOf(InsufficientBalanceException.class)
      .hasMessageContaining(account.id().get().toString());
    assertThat(account.balance()).isEqualTo(balance("100"));
    assertThat(account.history()).hasSize(1);
  }

  @Test
  void shouldListHistoryFromMostRecent() {
    var account = account();
    account.receiveTransfer(amount("100"), label("Salary"), NOW);
    account.pay(amount("20"), label("Bakery"), NOW.plusSeconds(60));
    account.pay(amount("30"), label("Books"), NOW.plusSeconds(120));

    assertThat(account.history())
      .extracting(operation -> operation.label().orElseThrow().get())
      .containsExactly("Books", "Bakery", "Salary");
  }
}
