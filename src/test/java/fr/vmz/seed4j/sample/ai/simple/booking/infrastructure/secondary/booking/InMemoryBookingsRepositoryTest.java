package fr.vmz.seed4j.sample.ai.simple.booking.infrastructure.secondary.booking;

import static fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.BookingsFixture.*;
import static fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.CatalogFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.BookingId;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.BookingToCreate;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryBookingsRepositoryTest {

  private final InMemoryBookingsRepository bookings = new InMemoryBookingsRepository();

  @Test
  void shouldGetSavedBooking() {
    var booking = massageTomorrowAt10();
    bookings.save(booking);

    assertThat(bookings.get(booking.id())).contains(booking);
    assertThat(bookings.get(BookingId.newId())).isEmpty();
  }

  @Test
  void shouldGetConfirmedBookingsOfServiceAndDay() {
    var massage = massageTomorrowAt10();
    var haircut = new BookingToCreate(HAIRCUT.id(), customer(), HAIRCUT.slotStartingAt(TOMORROW_10_AM)).create();
    var otherDay = massageAt(TOMORROW_10_AM.plusDays(1));
    var cancelled = massageAt(TOMORROW_10_AM.plusHours(2));
    cancelled.cancel(NOW);
    bookings.save(massage);
    bookings.save(haircut);
    bookings.save(otherDay);
    bookings.save(cancelled);

    assertThat(bookings.confirmedOf(MASSAGE.id(), TOMORROW)).containsExactly(massage);
    assertThat(bookings.confirmedOn(TOMORROW)).containsExactlyInAnyOrder(massage, haircut);
  }
}
