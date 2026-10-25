package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

public record Slot(LocalDate date, LocalTime arrival) {
  private static final Set<DayOfWeek> CLOSING_DAYS = Set.of(DayOfWeek.SUNDAY, DayOfWeek.MONDAY);
  private static final int MAX_DAYS_AHEAD = 30;
  private static final int MIN_HOURS_BEFORE_SERVICE = 2;

  public Slot {
    Assert.notNull("date", date);
    Assert.notNull("time", arrival);
    MealService.of(arrival);
  }

  public MealService service() {
    return MealService.of(arrival);
  }

  public LocalDateTime at() {
    return date.atTime(arrival);
  }

  public void assertBookableFrom(LocalDateTime now) {
    if (isClosingDay()) {
      throw new RestaurantClosedException(date);
    }

    if (isTooFarFrom(now)) {
      throw new BookingTooFarException(date);
    }

    if (isTooLateFrom(now)) {
      throw new BookingTooLateException(this);
    }
  }

  public boolean isBookableFrom(LocalDateTime now) {
    return !isClosingDay() && !isTooFarFrom(now) && !isTooLateFrom(now);
  }

  private boolean isClosingDay() {
    return CLOSING_DAYS.contains(date.getDayOfWeek());
  }

  private boolean isTooFarFrom(LocalDateTime now) {
    return date.isAfter(now.toLocalDate().plusDays(MAX_DAYS_AHEAD));
  }

  private boolean isTooLateFrom(LocalDateTime now) {
    return now.isAfter(date.atTime(service().start()).minusHours(MIN_HOURS_BEFORE_SERVICE));
  }
}
