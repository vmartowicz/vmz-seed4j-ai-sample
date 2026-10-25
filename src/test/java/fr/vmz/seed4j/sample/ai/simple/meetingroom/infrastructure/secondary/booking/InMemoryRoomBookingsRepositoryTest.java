package fr.vmz.seed4j.sample.ai.simple.meetingroom.infrastructure.secondary.booking;

import static fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking.RoomBookingsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking.EmployeeEmail;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking.RoomBookingId;
import fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room.RoomId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryRoomBookingsRepositoryTest {

  private static final RoomId EVEREST = RoomId.newId();
  private static final RoomId KILIMANJARO = RoomId.newId();

  private final InMemoryRoomBookingsRepository bookings = new InMemoryRoomBookingsRepository();

  @Test
  void shouldGetSavedBooking() {
    var booking = booking(EVEREST, TOMORROW_10_AM);
    bookings.save(booking);

    assertThat(bookings.get(booking.id())).contains(booking);
    assertThat(bookings.get(RoomBookingId.newId())).isEmpty();
  }

  @Test
  void shouldGetActiveBookingsOfRoom() {
    var active = booking(EVEREST, TOMORROW_10_AM);
    var cancelled = booking(EVEREST, TOMORROW_10_AM.plusHours(1));
    cancelled.cancel(jane(), NOW);
    bookings.save(active);
    bookings.save(cancelled);
    bookings.save(booking(KILIMANJARO, TOMORROW_10_AM));

    assertThat(bookings.activeForRoom(EVEREST)).containsExactly(active);
  }

  @Test
  void shouldGetActiveBookingsOfEmployee() {
    var janes = booking(EVEREST, TOMORROW_10_AM);
    var cancelled = booking(KILIMANJARO, TOMORROW_10_AM);
    cancelled.cancel(jane(), NOW);
    bookings.save(janes);
    bookings.save(cancelled);
    bookings.save(booking(KILIMANJARO, new EmployeeEmail("john@corp.com"), TOMORROW_10_AM.plusHours(1)));

    assertThat(bookings.activeForEmployee(jane())).containsExactly(janes);
  }
}
