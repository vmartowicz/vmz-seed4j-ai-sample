package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class DirectDebit {

  private static final int REFUND_WEEKS = 8;

  private final DirectDebitId id;
  private final Iban account;
  private final String creditor;
  private final BigDecimal amount;
  private final LocalDateTime collectedAt;
  private DirectDebitStatus status;

  private DirectDebit(Mandate mandate, BigDecimal amount, LocalDateTime collectedAt, DirectDebitStatus status) {
    id = DirectDebitId.newId();
    account = mandate.account();
    creditor = mandate.creditor();
    this.amount = amount;
    this.collectedAt = collectedAt;
    this.status = status;
  }

  public static DirectDebit collected(Mandate mandate, BigDecimal amount, LocalDateTime at) {
    return new DirectDebit(mandate, amount, at, DirectDebitStatus.COLLECTED);
  }

  public static DirectDebit rejected(Mandate mandate, BigDecimal amount, LocalDateTime at) {
    return new DirectDebit(mandate, amount, at, DirectDebitStatus.REJECTED);
  }

  public void refund(LocalDateTime now) {
    if (status != DirectDebitStatus.COLLECTED || now.isAfter(collectedAt.plusWeeks(REFUND_WEEKS))) {
      throw new DirectDebitNotRefundableException(id);
    }

    status = DirectDebitStatus.REFUNDED;
  }

  public DirectDebitId id() {
    return id;
  }

  public Iban account() {
    return account;
  }

  public String creditor() {
    return creditor;
  }

  public BigDecimal amount() {
    return amount;
  }

  public LocalDateTime collectedAt() {
    return collectedAt;
  }

  public DirectDebitStatus status() {
    return status;
  }
}
