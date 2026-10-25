package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TableBookingsRepository {
  void save(TableBooking booking);

  Optional<TableBooking> get(TableBookingId booking);

  List<TableBooking> on(LocalDate date);

  List<TableBooking> pending();

  default TableBooking booking(TableBookingId booking) {
    return get(booking).orElseThrow(() -> new UnknownTableBookingException(booking));
  }
}
