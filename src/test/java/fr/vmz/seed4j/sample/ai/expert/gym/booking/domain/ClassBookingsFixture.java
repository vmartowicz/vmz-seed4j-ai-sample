package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.time.LocalDateTime;

public final class ClassBookingsFixture {

  public static final LocalDateTime START = LocalDateTime.of(2026, 3, 12, 18, 0);
  public static final LocalDateTime NOW = START.minusDays(1);

  private ClassBookingsFixture() {}

  public static BookableSession session(int capacity) {
    return sessionAt(START, capacity);
  }

  public static BookableSession sessionAt(LocalDateTime start, int capacity) {
    return new BookableSession(SessionId.newId(), Gym.CONFLUENCE, start, capacity, false);
  }

  public static ClassBooking booked(MemberId member, BookableSession session) {
    return ClassBooking.request(member, session, NOW, false);
  }

  public static ClassBooking booked() {
    return booked(MemberId.newId(), session(10));
  }

  public static ClassBooking waiting() {
    return ClassBooking.request(MemberId.newId(), session(1), NOW, true);
  }

  public static ClassBooking noShowAt(MemberId member, LocalDateTime start) {
    var booking = booked(member, sessionAt(start, 10));
    booking.markNoShow(start);

    return booking;
  }
}
