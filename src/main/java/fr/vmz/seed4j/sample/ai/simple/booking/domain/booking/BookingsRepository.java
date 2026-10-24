package fr.vmz.seed4j.sample.ai.simple.booking.domain.booking;

import fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.ServiceId;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingsRepository {
  void save(Booking booking);

  Optional<Booking> get(BookingId booking);

  List<Booking> confirmedOf(ServiceId service, LocalDate day);

  List<Booking> confirmedOn(LocalDate day);

  default Booking booking(BookingId booking) {
    return get(booking).orElseThrow(() -> new UnknownBookingException(booking));
  }
}
