package fr.vmz.seed4j.sample.ai.expert.gym.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

public final class Invoice {

  private static final Money ARREARS_FEE = new Money(BigDecimal.TEN);
  private static final int MAX_ATTEMPTS = 3;
  private static final int FIRST_RETRY_DAYS = 3;
  private static final int SECOND_RETRY_DAYS = 7;

  private final InvoiceId id;
  private final MemberId member;
  private final InvoiceKind kind;
  private final YearMonth period;
  private final LocalDate issuedOn;
  private final Money amount;
  private InvoiceStatus status;
  private int attempts;
  private @Nullable LocalDate nextAttemptOn;

  private Invoice(MemberId member, InvoiceKind kind, LocalDate issuedOn, YearMonth period, Money amount, InvoiceStatus status) {
    id = InvoiceId.newId();
    this.member = member;
    this.kind = kind;
    this.issuedOn = issuedOn;
    this.period = period;
    this.amount = amount;
    this.status = status;
  }

  public static Invoice firstMonth(MemberId member, Money monthlyFee, Money signUpFee, LocalDate subscribedOn) {
    var remainingDays = subscribedOn.lengthOfMonth() - subscribedOn.getDayOfMonth() + 1;
    var amount = monthlyFee.prorata(remainingDays, subscribedOn.lengthOfMonth()).plus(signUpFee);

    return new Invoice(member, InvoiceKind.FIRST_MONTH, subscribedOn, YearMonth.from(subscribedOn), amount, InvoiceStatus.AWAITING_PAYMENT);
  }

  public static Invoice monthly(MemberId member, Money monthlyFee, YearMonth period, LocalDate today) {
    var invoice = new Invoice(member, InvoiceKind.MONTHLY, today, period, monthlyFee, InvoiceStatus.DEBIT_SUBMITTED);
    invoice.attempts = 1;

    return invoice;
  }

  public static Invoice arrearsFee(MemberId member, LocalDate today) {
    return new Invoice(member, InvoiceKind.ARREARS_FEE, today, YearMonth.from(today), ARREARS_FEE, InvoiceStatus.AWAITING_PAYMENT);
  }

  public void payByCard() {
    if (isPaid()) {
      throw new InvoiceAlreadyPaidException(id);
    }

    paid();
  }

  public void debitAccepted() {
    assertDebitInProgress();

    paid();
  }

  public boolean debitRejected(LocalDate today) {
    assertDebitInProgress();

    if (attempts == MAX_ATTEMPTS) {
      status = InvoiceStatus.UNPAID;
      return true;
    }

    status = InvoiceStatus.DEBIT_RETRY_SCHEDULED;
    nextAttemptOn = today.plusDays(attempts == 1 ? FIRST_RETRY_DAYS : SECOND_RETRY_DAYS);
    return false;
  }

  public boolean retryIfDue(LocalDate today) {
    if (nextAttemptOn == null || today.isBefore(nextAttemptOn)) {
      return false;
    }

    status = InvoiceStatus.DEBIT_SUBMITTED;
    attempts++;
    nextAttemptOn = null;
    return true;
  }

  public boolean isPaid() {
    return status == InvoiceStatus.PAID;
  }

  public boolean isDue() {
    return status == InvoiceStatus.UNPAID || status == InvoiceStatus.AWAITING_PAYMENT;
  }

  private void paid() {
    status = InvoiceStatus.PAID;
    nextAttemptOn = null;
  }

  private void assertDebitInProgress() {
    if (status != InvoiceStatus.DEBIT_SUBMITTED) {
      throw new NoDebitInProgressException(id, status);
    }
  }

  public InvoiceId id() {
    return id;
  }

  public MemberId member() {
    return member;
  }

  public InvoiceKind kind() {
    return kind;
  }

  public YearMonth period() {
    return period;
  }

  public LocalDate issuedOn() {
    return issuedOn;
  }

  public Money amount() {
    return amount;
  }

  public InvoiceStatus status() {
    return status;
  }

  public int attempts() {
    return attempts;
  }

  public Optional<LocalDate> nextAttemptOn() {
    return Optional.ofNullable(nextAttemptOn);
  }
}
