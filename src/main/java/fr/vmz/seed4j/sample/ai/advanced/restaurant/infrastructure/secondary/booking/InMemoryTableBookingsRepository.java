package fr.vmz.seed4j.sample.ai.advanced.restaurant.infrastructure.secondary.booking;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.BookingStatus;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.TableBooking;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.TableBookingId;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.TableBookingsRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryTableBookingsRepository implements TableBookingsRepository {

  private final Map<TableBookingId, TableBooking> bookings = new ConcurrentHashMap<>();

  @Override
  public void save(TableBooking booking) {
    bookings.put(booking.id(), booking);
  }

  @Override
  public Optional<TableBooking> get(TableBookingId booking) {
    return Optional.ofNullable(bookings.get(booking));
  }

  @Override
  public List<TableBooking> on(LocalDate date) {
    return bookings
      .values()
      .stream()
      .filter(booking -> booking.slot().date().equals(date))
      .toList();
  }

  @Override
  public List<TableBooking> pending() {
    return bookings
      .values()
      .stream()
      .filter(booking -> booking.status() == BookingStatus.PENDING)
      .toList();
  }
}
