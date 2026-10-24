package fr.vmz.seed4j.sample.ai.simple.booking.domain.booking;

import fr.vmz.seed4j.sample.ai.simple.booking.domain.Slot;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.ServiceId;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.ServicesCatalog;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDateTime;
import java.util.function.Predicate;

public class SlotsBooker {

  private final ServicesCatalog catalog;
  private final BookingsRepository bookings;

  public SlotsBooker(ServicesCatalog catalog, BookingsRepository bookings) {
    this.catalog = catalog;
    this.bookings = bookings;
  }

  public Booking book(ServiceId service, Customer customer, LocalDateTime start, LocalDateTime now) {
    var slot = catalog.service(service).slotStartingAt(start);
    assertAvailable(service, slot, now, other -> true);

    var booking = new BookingToCreate(service, customer, slot).create();
    bookings.save(booking);

    return booking;
  }

  public Booking changeSlot(BookingId bookingId, LocalDateTime start, LocalDateTime now) {
    var booking = bookings.booking(bookingId);
    var slot = catalog.service(booking.service()).slotStartingAt(start);
    assertAvailable(booking.service(), slot, now, other -> !other.id().equals(bookingId));

    booking.changeSlot(slot, now);
    bookings.save(booking);

    return booking;
  }

  private void assertAvailable(ServiceId service, Slot slot, LocalDateTime now, Predicate<Booking> otherBookings) {
    Assert.notNull("now", now);

    if (!slot.start().isAfter(now)) {
      throw new SlotInThePastException(slot);
    }

    var taken = bookings
      .confirmedOf(service, slot.start().toLocalDate())
      .stream()
      .filter(otherBookings)
      .anyMatch(other -> other.slot().overlaps(slot));
    if (taken) {
      throw new SlotAlreadyBookedException(slot);
    }
  }
}
