package fr.vmz.seed4j.sample.ai.simple.bank.domain;

import java.time.Instant;
import java.util.Optional;

public class AccountsTeller {

  private final AccountsRepository accounts;
  private final AuditTrail audit;

  public AccountsTeller(AccountsRepository accounts, AuditTrail audit) {
    this.accounts = accounts;
    this.audit = audit;
  }

  public Account open(HolderName holder, Instant now) {
    var account = Account.open(holder, now);
    accounts.save(account);

    audit.record(new AuditEvent(AuditEventType.ACCOUNT_OPENED, account.id(), Optional.empty(), account.balance(), now));

    return account;
  }

  public Operation receiveTransfer(AccountId accountId, Amount amount, Optional<Label> label, Instant now) {
    var account = accounts.account(accountId);
    var operation = account.receiveTransfer(amount, label, now);
    accounts.save(account);

    audit.record(auditEvent(AuditEventType.TRANSFER_RECEIVED, account, amount, now));

    return operation;
  }

  public Operation pay(AccountId accountId, Amount amount, Optional<Label> label, Instant now) {
    var account = accounts.account(accountId);

    try {
      var operation = account.pay(amount, label, now);
      accounts.save(account);

      audit.record(auditEvent(AuditEventType.PAYMENT_MADE, account, amount, now));

      return operation;
    } catch (InsufficientBalanceException e) {
      audit.record(auditEvent(AuditEventType.PAYMENT_REFUSED, account, amount, now));

      throw e;
    }
  }

  private static AuditEvent auditEvent(AuditEventType type, Account account, Amount amount, Instant now) {
    return new AuditEvent(type, account.id(), Optional.of(amount), account.balance(), now);
  }
}
