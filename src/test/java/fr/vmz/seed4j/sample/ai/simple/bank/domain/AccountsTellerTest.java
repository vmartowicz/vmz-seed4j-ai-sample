package fr.vmz.seed4j.sample.ai.simple.bank.domain;

import static fr.vmz.seed4j.sample.ai.simple.bank.domain.AccountsFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.util.Optional;
import org.junit.jupiter.api.Test;

@UnitTest
class AccountsTellerTest {

  private final AccountsRepository accounts = mock(AccountsRepository.class, CALLS_REAL_METHODS);
  private final AuditTrail audit = mock(AuditTrail.class);
  private final AccountsTeller teller = new AccountsTeller(accounts, audit);

  @Test
  void shouldAuditAccountOpening() {
    var account = teller.open(jane(), NOW);

    verify(accounts).save(account);
    verify(audit).record(new AuditEvent(AuditEventType.ACCOUNT_OPENED, account.id(), Optional.empty(), Balance.ZERO, NOW));
  }

  @Test
  void shouldAuditReceivedTransfer() {
    var account = existing(account());

    teller.receiveTransfer(account.id(), amount("100"), label("Salary"), NOW);

    verify(accounts).save(account);
    verify(audit).record(
      new AuditEvent(AuditEventType.TRANSFER_RECEIVED, account.id(), Optional.of(amount("100")), balance("100"), NOW)
    );
  }

  @Test
  void shouldAuditPayment() {
    var account = existing(accountWith("100"));

    teller.pay(account.id(), amount("40"), label("Bakery"), NOW);

    verify(accounts).save(account);
    verify(audit).record(new AuditEvent(AuditEventType.PAYMENT_MADE, account.id(), Optional.of(amount("40")), balance("60"), NOW));
  }

  @Test
  void shouldAuditRefusedPayment() {
    var account = existing(accountWith("100"));

    assertThatThrownBy(() -> teller.pay(account.id(), amount("150"), Optional.empty(), NOW)).isExactlyInstanceOf(
      InsufficientBalanceException.class
    );
    verify(accounts, never()).save(any());
    verify(audit).record(
      new AuditEvent(AuditEventType.PAYMENT_REFUSED, account.id(), Optional.of(amount("150")), balance("100"), NOW)
    );
  }

  @Test
  void shouldNotOperateUnknownAccount() {
    var unknown = AccountId.newId();
    doReturn(Optional.empty()).when(accounts).get(unknown);

    assertThatThrownBy(() -> teller.receiveTransfer(unknown, amount("10"), Optional.empty(), NOW))
      .isExactlyInstanceOf(UnknownAccountException.class)
      .hasMessageContaining(unknown.get().toString());
    verify(audit, never()).record(any());
  }

  private Account existing(Account account) {
    doReturn(Optional.of(account)).when(accounts).get(account.id());

    return account;
  }
}
