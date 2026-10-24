package fr.vmz.seed4j.sample.ai.simple.booking.application;

import fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.Booking;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.BookingId;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.BookingsRepository;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.Customer;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.RemindersSender;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.SlotsBooker;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.SmsSender;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.BookableService;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.ServiceId;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.ServicesCatalog;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookingsApplicationService {

  private final ServicesCatalog catalog;
  private final BookingsRepository bookings;
  private final SlotsBooker booker;
  private final RemindersSender reminders;
  private final Clock clock;

  public BookingsApplicationService(ServicesCatalog catalog, BookingsRepository bookings, SmsSender sms, Clock clock) {
    this.catalog = catalog;
    this.bookings = bookings;
    booker = new SlotsBooker(catalog, bookings);
    reminders = new RemindersSender(catalog, bookings, sms);
    this.clock = clock;
  }

  public List<BookableService> services() {
    return catalog.all();
  }

  public Booking book(ServiceId service, Customer customer, LocalDateTime start) {
    return booker.book(service, customer, start, now());
  }

  public Booking get(BookingId booking) {
    return bookings.booking(booking);
  }

  public Booking changeSlot(BookingId booking, LocalDateTime start) {
    return booker.changeSlot(booking, start, now());
  }

  public Booking cancel(BookingId bookingId) {
    var booking = bookings.booking(bookingId);
    booking.cancel(now());
    bookings.save(booking);

    return booking;
  }

  public void sendReminders() {
    reminders.sendRemindersFor(LocalDate.now(clock).plusDays(1));
  }

  private LocalDateTime now() {
    return LocalDateTime.now(clock);
  }
}
