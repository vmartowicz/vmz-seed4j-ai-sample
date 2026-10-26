package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;
import org.jspecify.annotations.Nullable;

public final class Transfer {

  private static final LocalTime CUT_OFF = LocalTime.of(16, 0);
  private static final Set<DayOfWeek> WEEK_END = Set.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

  private final TransferId id;
  private final Iban payer;
  private final String payeeName;
  private final Iban payeeIban;
  private final BigDecimal amount;
  private final String reason;
  private final TransferKind kind;
  private final LocalDateTime orderedAt;
  private LocalDate executionDate;
  private TransferStatus status;
  private @Nullable String rejection;

  private Transfer(TransferToOrder order, Payee payee, LocalDateTime now) {
    id = TransferId.newId();
    payer = order.payer();
    payeeName = payee.name();
    payeeIban = payee.iban();
    amount = order.amount();
    reason = order.reason().trim();
    kind = order.kind();
    orderedAt = now;
    executionDate = executionDateFor(kind, now);
    status = TransferStatus.SCHEDULED;
  }

  public static Transfer order(TransferToOrder order, Payee payee, LocalDateTime now) {
    return new Transfer(order, payee, now);
  }

  public static LocalDate executionDateFor(LocalDateTime orderedAt) {
    var businessDays = orderedAt.toLocalTime().isBefore(CUT_OFF) ? 1 : 2;
    var date = orderedAt.toLocalDate();
    for (int day = 0; day < businessDays; day++) {
      date = nextBusinessDay(date);
    }

    return date;
  }

  public void hold() {
    status = TransferStatus.HELD;
  }

  public void block() {
    status = TransferStatus.BLOCKED;
  }

  public void release(LocalDateTime now) {
    if (status != TransferStatus.HELD) {
      throw new TransferNotHeldException(id, status);
    }

    status = TransferStatus.SCHEDULED;
    executionDate = executionDateFor(kind, now);
  }

  public void executed() {
    status = TransferStatus.EXECUTED;
  }

  public void rejected(String why) {
    status = TransferStatus.REJECTED;
    rejection = why;
  }

  public boolean isDueOn(LocalDate date) {
    return status == TransferStatus.SCHEDULED && !executionDate.isAfter(date);
  }

  private static LocalDate executionDateFor(TransferKind kind, LocalDateTime now) {
    return kind == TransferKind.INSTANT ? now.toLocalDate() : executionDateFor(now);
  }

  private static LocalDate nextBusinessDay(LocalDate date) {
    var next = date.plusDays(1);
    while (WEEK_END.contains(next.getDayOfWeek())) {
      next = next.plusDays(1);
    }

    return next;
  }

  public TransferId id() {
    return id;
  }

  public Iban payer() {
    return payer;
  }

  public String payeeName() {
    return payeeName;
  }

  public Iban payeeIban() {
    return payeeIban;
  }

  public BigDecimal amount() {
    return amount;
  }

  public String reason() {
    return reason;
  }

  public TransferKind kind() {
    return kind;
  }

  public LocalDateTime orderedAt() {
    return orderedAt;
  }

  public LocalDate executionDate() {
    return executionDate;
  }

  public TransferStatus status() {
    return status;
  }

  public Optional<String> rejection() {
    return Optional.ofNullable(rejection);
  }
}
