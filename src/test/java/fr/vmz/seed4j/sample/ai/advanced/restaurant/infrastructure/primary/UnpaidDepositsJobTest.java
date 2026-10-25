package fr.vmz.seed4j.sample.ai.advanced.restaurant.infrastructure.primary;

import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.application.TableBookingsApplicationService;
import org.junit.jupiter.api.Test;

@UnitTest
class UnpaidDepositsJobTest {

  @Test
  void shouldCancelUnpaidBookings() {
    var bookings = mock(TableBookingsApplicationService.class);

    new UnpaidDepositsJob(bookings).cancelUnpaidBookings();

    verify(bookings).expireUnpaid();
  }
}
