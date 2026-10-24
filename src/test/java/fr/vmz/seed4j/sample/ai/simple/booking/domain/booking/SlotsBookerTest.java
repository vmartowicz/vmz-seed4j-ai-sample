package fr.vmz.seed4j.sample.ai.simple.booking.domain.booking;

import static fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.BookingsFixture.*;
import static fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.CatalogFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.ServiceId;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.ServicesCatalog;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.UnknownServiceException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class SlotsBookerTest {

  private final ServicesCatalog catalog = mock(ServicesCatalog.class, CALLS_REAL_METHODS);
  private final BookingsRepository bookings = mock(BookingsRepository.class, CALLS_REAL_METHODS);
  private final SlotsBooker booker = new SlotsBooker(catalog, bookings);

  @BeforeEach
  void loadCatalog() {
    doReturn(Optional.of(MASSAGE)).when(catalog).get(MASSAGE.id());
    doReturn(List.of()).when(bookings).confirmedOf(any(), any());
  }

  @Nested
  class Book {

    @Test
    void shouldBookFreeSlot() {
      var booking = booker.book(MASSAGE.id(), customer(), TOMORROW_10_AM, NOW);

      assertThat(booking.slot()).isEqualTo(massageSlotAt(TOMORROW_10_AM));
      assertThat(booking.status()).isEqualTo(BookingStatus.CONFIRMED);
      verify(bookings).save(booking);
    }

    @Test
    void shouldNotBookUnknownService() {
      var unknownService = new ServiceId(UUID.randomUUID());
      doReturn(Optional.empty()).when(catalog).get(unknownService);

      assertThatThrownBy(() -> booker.book(unknownService, customer(), TOMORROW_10_AM, NOW))
        .isExactlyInstanceOf(UnknownServiceException.class)
        .hasMessageContaining(unknownService.get().toString());
    }

    @Test
    void shouldNotBookSlotStartingNow() {
      assertThatThrownBy(() -> booker.book(MASSAGE.id(), customer(), NOW, NOW)).isExactlyInstanceOf(SlotInThePastException.class);
      verify(bookings, never()).save(any());
    }

    @Test
    void shouldNotBookOverlappingSlot() {
      doReturn(List.of(massageTomorrowAt10())).when(bookings).confirmedOf(MASSAGE.id(), TOMORROW);

      assertThatThrownBy(() -> booker.book(MASSAGE.id(), customer(), TOMORROW_10_AM, NOW)).isExactlyInstanceOf(
        SlotAlreadyBookedException.class
      );
      verify(bookings, never()).save(any());
    }

    @Test
    void shouldBookFollowingSlot() {
      doReturn(List.of(massageTomorrowAt10())).when(bookings).confirmedOf(MASSAGE.id(), TOMORROW);

      assertThat(booker.book(MASSAGE.id(), customer(), TOMORROW_10_AM.plusHours(1), NOW).slot().start()).isEqualTo(
        TOMORROW_10_AM.plusHours(1)
      );
    }
  }

  @Nested
  class ChangeSlot {

    @Test
    void shouldChangeSlot() {
      var booking = existingMassageTomorrowAt10();

      var changed = booker.changeSlot(booking.id(), TOMORROW_10_AM.plusHours(3), NOW);

      assertThat(changed.slot().start()).isEqualTo(TOMORROW_10_AM.plusHours(3));
      verify(bookings).save(booking);
    }

    @Test
    void shouldChangeSlotOverlappingItsOwnSlot() {
      var booking = existingMassageTomorrowAt10();
      doReturn(List.of(booking)).when(bookings).confirmedOf(MASSAGE.id(), TOMORROW);

      assertThat(booker.changeSlot(booking.id(), TOMORROW_10_AM, NOW).slot().start()).isEqualTo(TOMORROW_10_AM);
    }

    @Test
    void shouldNotChangeToSlotOfAnotherBooking() {
      var booking = existingMassageTomorrowAt10();
      doReturn(List.of(booking, massageAt(TOMORROW_10_AM.plusHours(3)))).when(bookings).confirmedOf(MASSAGE.id(), TOMORROW);

      assertThatThrownBy(() -> booker.changeSlot(booking.id(), TOMORROW_10_AM.plusHours(3), NOW)).isExactlyInstanceOf(
        SlotAlreadyBookedException.class
      );
      assertThat(booking.slot().start()).isEqualTo(TOMORROW_10_AM);
    }

    @Test
    void shouldNotChangeUnknownBooking() {
      var unknownBooking = BookingId.newId();
      doReturn(Optional.empty()).when(bookings).get(unknownBooking);

      assertThatThrownBy(() -> booker.changeSlot(unknownBooking, TOMORROW_10_AM, NOW))
        .isExactlyInstanceOf(UnknownBookingException.class)
        .hasMessageContaining(unknownBooking.get().toString());
    }

    private Booking existingMassageTomorrowAt10() {
      var booking = massageTomorrowAt10();
      doReturn(Optional.of(booking)).when(bookings).get(booking.id());

      return booking;
    }
  }
}
