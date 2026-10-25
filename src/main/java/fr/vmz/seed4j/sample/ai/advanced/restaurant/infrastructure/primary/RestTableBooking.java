package fr.vmz.seed4j.sample.ai.advanced.restaurant.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.BookingStatus;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.MealService;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.TableBooking;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestTableBooking(
  UUID id,
  LocalDate date,
  LocalTime time,
  MealService service,
  int people,
  String fullName,
  String email,
  String phone,
  @Nullable String comments,
  BookingStatus status,
  List<RestTable> tables,
  @Nullable RestDeposit deposit
) {
  static RestTableBooking from(TableBooking booking) {
    var contact = booking.contact();

    return new RestTableBooking(
      booking.id().get(),
      booking.slot().date(),
      booking.slot().arrival(),
      booking.slot().service(),
      booking.party().get(),
      contact.fullName(),
      contact.email().get(),
      contact.phone().get(),
      booking.comments().orElse(null),
      booking.status(),
      RestTable.from(booking.tables()),
      booking.deposit().map(RestDeposit::from).orElse(null)
    );
  }
}
