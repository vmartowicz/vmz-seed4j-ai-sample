package fr.vmz.seed4j.sample.ai.simple.booking.domain.booking;

import fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.ServicesCatalog;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RemindersSender {

  private static final DateTimeFormatter HOUR = DateTimeFormatter.ofPattern("HH:mm");

  private final ServicesCatalog catalog;
  private final BookingsRepository bookings;
  private final SmsSender sms;

  public RemindersSender(ServicesCatalog catalog, BookingsRepository bookings, SmsSender sms) {
    this.catalog = catalog;
    this.bookings = bookings;
    this.sms = sms;
  }

  public void sendRemindersFor(LocalDate day) {
    bookings
      .confirmedOn(day)
      .stream()
      .filter(booking -> booking.needsReminderOn(day))
      .forEach(this::remind);
  }

  private void remind(Booking booking) {
    var service = catalog.service(booking.service());
    var text = "Rappel : votre rendez-vous " + service.name().get() + " demain à " + booking.slot().start().format(HOUR);

    sms.send(new Sms(booking.customer().phone(), text));
    booking.markReminderSent();
    bookings.save(booking);
  }
}
