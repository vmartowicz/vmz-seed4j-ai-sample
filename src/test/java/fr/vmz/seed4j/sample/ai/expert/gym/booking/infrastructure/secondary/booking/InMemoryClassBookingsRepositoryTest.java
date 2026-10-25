package fr.vmz.seed4j.sample.ai.expert.gym.booking.infrastructure.secondary.booking;

import static fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.ClassBookingsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.ClassBookingId;
import fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.SessionId;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryClassBookingsRepositoryTest {

  private final InMemoryClassBookingsRepository bookings = new InMemoryClassBookingsRepository();

  @Test
  void shouldFindBookingsOfMemberAndSession() {
    var member = MemberId.newId();
    var session = session(10);
    var booking = booked(member, session);
    bookings.save(booking);
    bookings.save(booked());

    assertThat(bookings.get(booking.id())).containsSame(booking);
    assertThat(bookings.get(ClassBookingId.newId())).isEmpty();
    assertThat(bookings.of(member)).containsExactly(booking);
    assertThat(bookings.ofSession(session.id())).containsExactly(booking);
    assertThat(bookings.ofSession(SessionId.newId())).isEmpty();
  }
}
