package fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import java.time.LocalDateTime;
import java.util.Set;

public class ReservationDesk {

  private static final Set<Category> FREE_DAY_CATEGORIES = Set.of(Category.ECO, Category.COMFORT);

  private final ReservationsRepository reservations;
  private final LoyaltyCard loyalty;

  public ReservationDesk(ReservationsRepository reservations, LoyaltyCard loyalty) {
    this.reservations = reservations;
    this.loyalty = loyalty;
  }

  public Reservation book(ReservationToBook booking, LocalDateTime now) {
    Assert.notNull("booking", booking);

    var pickUpAt = booking.period().pickUpAt();
    if (!pickUpAt.isAfter(now)) {
      throw new PickUpInThePastException(pickUpAt);
    }

    var driver = booking.driver();
    driver.assertCanDriveOn(pickUpAt.toLocalDate());
    if (driver.isYoungOn(pickUpAt.toLocalDate()) && booking.category().isForbiddenToYoungDrivers()) {
      throw new YoungDriverCategoryException(booking.category());
    }

    if (booking.freeDay() && !FREE_DAY_CATEGORIES.contains(booking.category())) {
      throw new FreeDayCategoryException(booking.category());
    }

    var reservation = booking.book(loyalty.isGold(driver.email()), now);
    if (booking.freeDay() && !loyalty.redeemFreeDay(driver.email(), reservation.id().get())) {
      throw new NotEnoughLoyaltyPointsException(driver.email().get());
    }

    reservations.save(reservation);
    return reservation;
  }

  public Reservation cancel(ReservationId id, LocalDateTime now) {
    var reservation = reservations.reservation(id);

    reservation.cancel(now);
    if (reservation.freeDay()) {
      loyalty.cancelRedemption(reservation.driver().email(), reservation.id().get());
    }
    reservations.save(reservation);

    return reservation;
  }

  public Reservation markPickedUp(ReservationId id) {
    var reservation = reservations.reservation(id);

    reservation.markPickedUp();
    reservations.save(reservation);

    return reservation;
  }

  public Reservation reservation(ReservationId id) {
    return reservations.reservation(id);
  }
}
