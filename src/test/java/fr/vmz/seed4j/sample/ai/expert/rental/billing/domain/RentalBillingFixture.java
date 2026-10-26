package fr.vmz.seed4j.sample.ai.expert.rental.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public final class RentalBillingFixture {

  public static final LocalDateTime PICKED_UP = LocalDateTime.of(2026, 6, 10, 9, 0);
  public static final LocalDateTime PLANNED_RETURN = PICKED_UP.plusDays(3);
  public static final LocalDate RETURN_DAY = PLANNED_RETURN.toLocalDate();
  public static final Email JANE = new Email("jane@mail.com");

  private RentalBillingFixture() {}

  public static ReturnReport onTime() {
    return returned(PLANNED_RETURN, 600, RentalPoint.PART_DIEU);
  }

  public static ReturnReport returned(LocalDateTime at, int kilometres, RentalPoint point) {
    return report(Category.ECO, at, kilometres, point, BigDecimal.ZERO, false, false);
  }

  public static ReturnReport report(
    Category category,
    LocalDateTime at,
    int kilometres,
    RentalPoint point,
    BigDecimal missingFuel,
    boolean electric,
    boolean damaged
  ) {
    return new ReturnReport(
      UUID.randomUUID(),
      JANE,
      category,
      new BigDecimal("35"),
      3,
      new BigDecimal("105"),
      PICKED_UP,
      PLANNED_RETURN,
      at,
      RentalPoint.PART_DIEU,
      point,
      kilometres,
      missingFuel,
      electric,
      damaged
    );
  }

  public static Deposit held() {
    return new Deposit(UUID.randomUUID(), JANE, new BigDecimal("800"));
  }

  public static Deposit returned(boolean damaged) {
    var deposit = held();
    deposit.returned(RETURN_DAY, damaged);

    return deposit;
  }
}
