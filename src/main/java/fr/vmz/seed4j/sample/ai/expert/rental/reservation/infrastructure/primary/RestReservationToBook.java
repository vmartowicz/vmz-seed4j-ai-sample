package fr.vmz.seed4j.sample.ai.expert.rental.reservation.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.Options;
import fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.RentalPeriod;
import fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.ReservationToBook;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.time.LocalDateTime;
import org.jspecify.annotations.Nullable;

record RestReservationToBook(
  RestDriver driver,
  @Nullable Category category,
  @Nullable RentalPoint pickUpPoint,
  @Nullable LocalDateTime pickUpAt,
  @Nullable RentalPoint returnPoint,
  @Nullable LocalDateTime returnAt,
  @Nullable Boolean childSeat,
  @Nullable Boolean additionalDriver,
  @Nullable Boolean gps,
  @Nullable Boolean freeDay
) {
  ReservationToBook toDomain() {
    return new ReservationToBook(
      driver.toDomain(),
      category,
      new RentalPeriod(pickUpPoint, pickUpAt, returnPoint, returnAt),
      new Options(Boolean.TRUE.equals(childSeat), Boolean.TRUE.equals(additionalDriver), Boolean.TRUE.equals(gps)),
      Boolean.TRUE.equals(freeDay)
    );
  }
}
