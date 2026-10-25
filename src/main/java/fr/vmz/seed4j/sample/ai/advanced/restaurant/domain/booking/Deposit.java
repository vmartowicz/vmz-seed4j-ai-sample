package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table.PartySize;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Deposit(BigDecimal amount, LocalDateTime dueBy, DepositStatus status) {
  private static final BigDecimal PER_PERSON = BigDecimal.valueOf(20);
  private static final int PAYMENT_HOURS = 48;

  public Deposit {
    Assert.field("amount", amount).strictlyPositive();
    Assert.notNull("dueBy", dueBy);
    Assert.notNull("status", status);
  }

  public static Deposit expected(PartySize party, LocalDateTime bookedAt, LocalDateTime mealAt) {
    var deadline = bookedAt.plusHours(PAYMENT_HOURS);
    var dueBy = deadline.isBefore(mealAt) ? deadline : mealAt;

    return new Deposit(PER_PERSON.multiply(BigDecimal.valueOf(party.get())), dueBy, DepositStatus.EXPECTED);
  }

  public boolean accepts(BigDecimal payment) {
    return amount.compareTo(payment) == 0;
  }

  public boolean isPaid() {
    return status == DepositStatus.PAID;
  }

  public Deposit paid() {
    return withStatus(DepositStatus.PAID);
  }

  public Deposit refunded() {
    return withStatus(DepositStatus.REFUNDED);
  }

  public Deposit forfeited() {
    return withStatus(DepositStatus.FORFEITED);
  }

  private Deposit withStatus(DepositStatus newStatus) {
    return new Deposit(amount, dueBy, newStatus);
  }
}
