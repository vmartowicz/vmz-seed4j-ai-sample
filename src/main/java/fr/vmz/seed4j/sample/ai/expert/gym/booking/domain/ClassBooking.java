package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.time.LocalDateTime;
import java.util.Set;

public final class ClassBooking {

  private static final int FREE_CANCELLATION_HOURS = 2;
  private static final Set<ClassBookingStatus> IN_QUOTA = Set.of(
    ClassBookingStatus.BOOKED,
    ClassBookingStatus.LATE_CANCELLED,
    ClassBookingStatus.NO_SHOW
  );

  private final ClassBookingId id;
  private final MemberId member;
  private final BookableSession session;
  private final LocalDateTime requestedAt;
  private ClassBookingStatus status;

  private ClassBooking(MemberId member, BookableSession session, LocalDateTime requestedAt, ClassBookingStatus status) {
    Assert.notNull("member", member);
    Assert.notNull("session", session);

    id = ClassBookingId.newId();
    this.member = member;
    this.session = session;
    this.requestedAt = requestedAt;
    this.status = status;
  }

  public static ClassBooking request(MemberId member, BookableSession session, LocalDateTime now, boolean full) {
    return new ClassBooking(member, session, now, full ? ClassBookingStatus.WAITING : ClassBookingStatus.BOOKED);
  }

  public boolean cancel(LocalDateTime now) {
    if (status == ClassBookingStatus.WAITING) {
      status = ClassBookingStatus.CANCELLED;
      return false;
    }

    assertStatus(ClassBookingStatus.BOOKED, "cancel");
    if (!now.isBefore(session.start())) {
      throw new ClassAlreadyStartedException(id);
    }

    var free = !now.isAfter(session.start().minusHours(FREE_CANCELLATION_HOURS));
    status = free ? ClassBookingStatus.CANCELLED : ClassBookingStatus.LATE_CANCELLED;
    return true;
  }

  public void promote() {
    assertStatus(ClassBookingStatus.WAITING, "promote");

    status = ClassBookingStatus.BOOKED;
  }

  public void markNoShow(LocalDateTime now) {
    assertStatus(ClassBookingStatus.BOOKED, "mark as no-show");
    if (now.isBefore(session.start())) {
      throw new ClassNotStartedException(id);
    }

    status = ClassBookingStatus.NO_SHOW;
  }

  public boolean cancelByGym() {
    if (!isActive()) {
      return false;
    }

    status = ClassBookingStatus.CANCELLED_BY_GYM;
    return true;
  }

  public boolean holdsSeat() {
    return status == ClassBookingStatus.BOOKED;
  }

  public boolean isActive() {
    return holdsSeat() || status == ClassBookingStatus.WAITING;
  }

  public boolean countsInQuota() {
    return IN_QUOTA.contains(status);
  }

  public boolean isNoShow() {
    return status == ClassBookingStatus.NO_SHOW || status == ClassBookingStatus.LATE_CANCELLED;
  }

  private void assertStatus(ClassBookingStatus expected, String action) {
    if (status != expected) {
      throw new InvalidClassBookingTransitionException(id, status, action);
    }
  }

  public ClassBookingId id() {
    return id;
  }

  public MemberId member() {
    return member;
  }

  public BookableSession session() {
    return session;
  }

  public LocalDateTime requestedAt() {
    return requestedAt;
  }

  public ClassBookingStatus status() {
    return status;
  }
}
