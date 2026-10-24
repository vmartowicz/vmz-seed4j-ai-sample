package fr.vmz.seed4j.sample.ai.simple.booking.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.simple.booking.application.BookingsApplicationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class BookingRemindersJob {

  private final BookingsApplicationService bookings;

  BookingRemindersJob(BookingsApplicationService bookings) {
    this.bookings = bookings;
  }

  @Scheduled(cron = "0 0 18 * * *", zone = "Europe/Paris")
  void sendReminders() {
    bookings.sendReminders();
  }
}
