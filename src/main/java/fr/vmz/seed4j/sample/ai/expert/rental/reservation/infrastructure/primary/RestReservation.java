package fr.vmz.seed4j.sample.ai.expert.rental.reservation.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.Reservation;
import fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.ReservationStatus;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestReservation(
  UUID id,
  String driverName,
  String driverEmail,
  Category category,
  RentalPoint pickUpPoint,
  LocalDateTime pickUpAt,
  RentalPoint returnPoint,
  LocalDateTime returnAt,
  int days,
  BigDecimal dailyRate,
  BigDecimal youngDriverFee,
  BigDecimal options,
  BigDecimal freeDay,
  BigDecimal total,
  LocalDateTime bookedAt,
  ReservationStatus status,
  @Nullable BigDecimal refund
) {
  static RestReservation from(Reservation reservation) {
    var period = reservation.period();
    var quote = reservation.quote();

    return new RestReservation(
      reservation.id().get(),
      reservation.driver().name(),
      reservation.driver().email().get(),
      reservation.category(),
      period.pickUpPoint(),
      period.pickUpAt(),
      period.returnPoint(),
      period.returnAt(),
      quote.days(),
      quote.dailyRate(),
      quote.youngDriverFee(),
      quote.options(),
      quote.freeDay(),
      quote.total(),
      reservation.bookedAt(),
      reservation.status(),
      reservation.refund().orElse(null)
    );
  }
}
