package fr.vmz.seed4j.sample.ai.simple.booking.infrastructure.primary;

import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.simple.booking.application.BookingsApplicationService;
import org.junit.jupiter.api.Test;

@UnitTest
class BookingRemindersJobTest {

  @Test
  void shouldSendReminders() {
    var bookings = mock(BookingsApplicationService.class);

    new BookingRemindersJob(bookings).sendReminders();

    verify(bookings).sendReminders();
  }
}
