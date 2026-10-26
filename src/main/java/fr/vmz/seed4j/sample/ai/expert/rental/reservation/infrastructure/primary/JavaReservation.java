package fr.vmz.seed4j.sample.ai.expert.rental.reservation.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.Reservation;
import fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.ReservationStatus;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record JavaReservation(
  UUID id,
  String driverEmail,
  boolean youngDriver,
  Category category,
  RentalPoint pickUpPoint,
  LocalDateTime pickUpAt,
  LocalDateTime returnAt,
  int days,
  BigDecimal dailyRate,
  BigDecimal paid,
  boolean confirmed
) {
  static JavaReservation from(Reservation reservation) {
    var period = reservation.period();
    var quote = reservation.quote();

    return new JavaReservation(
      reservation.id().get(),
      reservation.driver().email().get(),
      reservation.isYoungDriver(),
      reservation.category(),
      period.pickUpPoint(),
      period.pickUpAt(),
      period.returnAt(),
      quote.days(),
      quote.dailyRate(),
      quote.total(),
      reservation.status() == ReservationStatus.CONFIRMED
    );
  }
}
