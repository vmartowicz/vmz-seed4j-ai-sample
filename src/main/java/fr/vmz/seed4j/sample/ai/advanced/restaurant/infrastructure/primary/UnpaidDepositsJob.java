package fr.vmz.seed4j.sample.ai.advanced.restaurant.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.application.TableBookingsApplicationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class UnpaidDepositsJob {

  private final TableBookingsApplicationService bookings;

  UnpaidDepositsJob(TableBookingsApplicationService bookings) {
    this.bookings = bookings;
  }

  @Scheduled(cron = "0 */5 * * * *", zone = "Europe/Paris")
  void cancelUnpaidBookings() {
    bookings.expireUnpaid();
  }
}
