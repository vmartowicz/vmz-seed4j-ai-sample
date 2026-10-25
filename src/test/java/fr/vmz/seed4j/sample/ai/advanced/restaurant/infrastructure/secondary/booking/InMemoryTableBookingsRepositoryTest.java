package fr.vmz.seed4j.sample.ai.advanced.restaurant.infrastructure.secondary.booking;

import static fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.TableBookingsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.TableBookingId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryTableBookingsRepositoryTest {

  private final InMemoryTableBookingsRepository bookings = new InMemoryTableBookingsRepository();

  @Test
  void shouldGetSavedBooking() {
    var booking = confirmedBooking();

    bookings.save(booking);

    assertThat(bookings.get(booking.id())).containsSame(booking);
    assertThat(bookings.get(TableBookingId.newId())).isEmpty();
  }

  @Test
  void shouldListBookingsOfDate() {
    var booking = confirmedBooking();
    bookings.save(booking);

    assertThat(bookings.on(TUESDAY)).containsExactly(booking);
    assertThat(bookings.on(TUESDAY.plusDays(1))).isEmpty();
  }

  @Test
  void shouldListPendingBookings() {
    var pending = pendingBooking();
    bookings.save(pending);
    bookings.save(confirmedBooking());

    assertThat(bookings.pending()).containsExactly(pending);
  }
}
