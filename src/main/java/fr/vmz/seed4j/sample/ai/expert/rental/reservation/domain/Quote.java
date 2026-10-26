package fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain;

import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import java.math.BigDecimal;

public record Quote(int days, BigDecimal dailyRate, BigDecimal rental, BigDecimal youngDriverFee, BigDecimal options, BigDecimal freeDay) {
  private static final BigDecimal YOUNG_DRIVER_PER_DAY = new BigDecimal("15");
  private static final BigDecimal CHILD_SEAT_PER_DAY = new BigDecimal("5");
  private static final BigDecimal ADDITIONAL_DRIVER_PER_DAY = new BigDecimal("10");
  private static final BigDecimal GPS_PER_DAY = new BigDecimal("8");
  private static final BigDecimal GPS_CAP = new BigDecimal("50");

  public static Quote of(Category category, int days, boolean youngDriver, Options options, boolean gold, boolean freeDay) {
    var rate = DailyRates.of(category);
    var count = BigDecimal.valueOf(days);

    var extras = BigDecimal.ZERO;
    if (options.childSeat()) {
      extras = extras.add(CHILD_SEAT_PER_DAY.multiply(count));
    }

    if (options.additionalDriver() && !gold) {
      extras = extras.add(ADDITIONAL_DRIVER_PER_DAY.multiply(count));
    }

    if (options.gps()) {
      extras = extras.add(GPS_PER_DAY.multiply(count).min(GPS_CAP));
    }

    return new Quote(
      days,
      rate,
      rate.multiply(count),
      youngDriver ? YOUNG_DRIVER_PER_DAY.multiply(count) : BigDecimal.ZERO,
      extras,
      freeDay ? rate : BigDecimal.ZERO
    );
  }

  public BigDecimal total() {
    return rental.add(youngDriverFee).add(options).subtract(freeDay);
  }
}
