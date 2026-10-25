package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking;

import static fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking.RoomBookingsFixture.*;
import static fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.RoomsFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.Room;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.RoomId;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.RoomsRepository;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.UnknownRoomException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class RoomsBookerTest {

  private static final Room EVEREST = everest();

  private final RoomsRepository rooms = mock(RoomsRepository.class, CALLS_REAL_METHODS);
  private final RoomBookingsRepository bookings = mock(RoomBookingsRepository.class, CALLS_REAL_METHODS);
  private final RoomsBooker booker = new RoomsBooker(rooms, bookings);

  @BeforeEach
  void loadRooms() {
    doReturn(Optional.of(EVEREST)).when(rooms).get(EVEREST.id());
    doReturn(List.of()).when(bookings).activeForRoom(any());
  }

  @Nested
  class Book {

    @Test
    void shouldBookFreeSlot() {
      var booking = booker.book(EVEREST.id(), jane(), new HourSlot(TOMORROW_10_AM), NOW);

      assertThat(booking.room()).isEqualTo(EVEREST.id());
      verify(bookings).save(booking);
    }

    @Test
    void shouldNotBookUnknownRoom() {
      var unknownRoom = RoomId.newId();
      doReturn(Optional.empty()).when(rooms).get(unknownRoom);

      assertThatThrownBy(() -> booker.book(unknownRoom, jane(), new HourSlot(TOMORROW_10_AM), NOW))
        .isExactlyInstanceOf(UnknownRoomException.class)
        .hasMessageContaining(unknownRoom.get().toString());
    }

    @Test
    void shouldNotBookSlotAlreadyStarted() {
      assertThatThrownBy(() -> booker.book(EVEREST.id(), jane(), new HourSlot(NOW), NOW)).isExactlyInstanceOf(
        SlotAlreadyStartedException.class
      );
      verify(bookings, never()).save(any());
    }

    @Test
    void shouldNotBookRoomTwiceOnTheSameSlot() {
      doReturn(List.of(booking(EVEREST.id(), new EmployeeEmail("john@corp.com"), TOMORROW_10_AM))).when(bookings).activeForRoom(EVEREST.id());

      assertThatThrownBy(() -> booker.book(EVEREST.id(), jane(), new HourSlot(TOMORROW_10_AM), NOW)).isExactlyInstanceOf(
        RoomAlreadyBookedException.class
      );
      verify(bookings, never()).save(any());
    }

    @Test
    void shouldBookFollowingSlot() {
      doReturn(List.of(booking(EVEREST.id(), TOMORROW_10_AM))).when(bookings).activeForRoom(EVEREST.id());

      assertThat(booker.book(EVEREST.id(), jane(), new HourSlot(TOMORROW_10_AM.plusHours(1)), NOW).slot().start()).isEqualTo(
        TOMORROW_10_AM.plusHours(1)
      );
    }
  }

  @Test
  void shouldCancelBooking() {
    var booking = booking(EVEREST.id(), TOMORROW_10_AM);
    doReturn(Optional.of(booking)).when(bookings).get(booking.id());

    booker.cancel(booking.id(), jane(), NOW);

    assertThat(booking.status()).isEqualTo(RoomBookingStatus.CANCELLED);
    verify(bookings).save(booking);
  }

  @Test
  void shouldNotCancelUnknownBooking() {
    var unknown = RoomBookingId.newId();
    doReturn(Optional.empty()).when(bookings).get(unknown);

    assertThatThrownBy(() -> booker.cancel(unknown, jane(), NOW))
      .isExactlyInstanceOf(UnknownRoomBookingException.class)
      .hasMessageContaining(unknown.get().toString());
  }

  @Test
  void shouldListCurrentBookingsByStart() {
    var ended = booking(EVEREST.id(), NOW.minusHours(1));
    var later = booking(EVEREST.id(), TOMORROW_10_AM);
    var ongoing = booking(EVEREST.id(), NOW);
    doReturn(List.of(ended, later, ongoing)).when(bookings).activeForEmployee(jane());

    assertThat(booker.currentBookingsOf(jane(), NOW.plusMinutes(30))).containsExactly(ongoing, later);
  }
}
