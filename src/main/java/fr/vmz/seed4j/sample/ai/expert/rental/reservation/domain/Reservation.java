package fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain;

import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

public final class Reservation {

  private static final int FREE_CANCELLATION_HOURS = 48;

  private final ReservationId id;
  private final Driver driver;
  private final Category category;
  private final RentalPeriod period;
  private final Options options;
  private final boolean freeDay;
  private final Quote quote;
  private final boolean youngDriver;
  private final LocalDateTime bookedAt;
  private ReservationStatus status;
  private @Nullable BigDecimal refund;

  Reservation(ReservationToBook booking, Quote quote, boolean youngDriver, LocalDateTime bookedAt) {
    id = ReservationId.newId();
    driver = booking.driver();
    category = booking.category();
    period = booking.period();
    options = booking.options();
    freeDay = booking.freeDay();
    this.quote = quote;
    this.youngDriver = youngDriver;
    this.bookedAt = bookedAt;
    status = ReservationStatus.CONFIRMED;
  }

  public void cancel(LocalDateTime now) {
    assertConfirmed("cancel");

    var paid = quote.total();
    refund = now.isBefore(period.pickUpAt().minusHours(FREE_CANCELLATION_HOURS)) ? paid : paid.subtract(quote.dailyRate()).max(BigDecimal.ZERO);
    status = ReservationStatus.CANCELLED;
  }

  public void markPickedUp() {
    assertConfirmed("pick up");

    status = ReservationStatus.PICKED_UP;
  }

  private void assertConfirmed(String action) {
    if (status != ReservationStatus.CONFIRMED) {
      throw new InvalidReservationTransitionException(id, status, action);
    }
  }

  public ReservationId id() {
    return id;
  }

  public Driver driver() {
    return driver;
  }

  public Category category() {
    return category;
  }

  public RentalPeriod period() {
    return period;
  }

  public Options options() {
    return options;
  }

  public boolean freeDay() {
    return freeDay;
  }

  public Quote quote() {
    return quote;
  }

  public boolean isYoungDriver() {
    return youngDriver;
  }

  public LocalDateTime bookedAt() {
    return bookedAt;
  }

  public ReservationStatus status() {
    return status;
  }

  public Optional<BigDecimal> refund() {
    return Optional.ofNullable(refund);
  }
}
