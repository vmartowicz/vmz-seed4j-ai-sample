package fr.vmz.seed4j.sample.ai.simple.booking.infrastructure.secondary.booking;

import fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.Booking;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.BookingId;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.BookingStatus;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.BookingsRepository;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.ServiceId;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryBookingsRepository implements BookingsRepository {

  private final Map<BookingId, Booking> bookings = new ConcurrentHashMap<>();

  @Override
  public void save(Booking booking) {
    bookings.put(booking.id(), booking);
  }

  @Override
  public Optional<Booking> get(BookingId booking) {
    return Optional.ofNullable(bookings.get(booking));
  }

  @Override
  public List<Booking> confirmedOf(ServiceId service, LocalDate day) {
    return confirmedOn(day)
      .stream()
      .filter(booking -> booking.service().equals(service))
      .toList();
  }

  @Override
  public List<Booking> confirmedOn(LocalDate day) {
    return bookings
      .values()
      .stream()
      .filter(booking -> booking.status() == BookingStatus.CONFIRMED)
      .filter(booking -> booking.slot().start().toLocalDate().equals(day))
      .toList();
  }
}
