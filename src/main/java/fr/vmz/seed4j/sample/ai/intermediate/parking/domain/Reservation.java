package fr.vmz.seed4j.sample.ai.intermediate.parking.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class Reservation {

  private final ReservationId id;
  private final ResidentName resident;
  private final SpaceNumber space;
  private final LocalDate date;
  private ReservationStatus status = ReservationStatus.ACTIVE;

  Reservation(ReservationId id, ReservationToCreate reservation) {
    this.id = id;
    resident = reservation.resident();
    space = reservation.space();
    date = reservation.date();
  }

  public void cancel(LocalDateTime now) {
    Assert.notNull("now", now);

    if (status == ReservationStatus.CANCELLED) {
      throw new ReservationAlreadyCancelledException(id);
    }

    if (now.isAfter(date.minusDays(1).atStartOfDay())) {
      throw new CancellationDeadlinePassedException(id);
    }

    status = ReservationStatus.CANCELLED;
  }

  public boolean isCurrentOn(LocalDate today) {
    return status == ReservationStatus.ACTIVE && !date.isBefore(today);
  }

  public ReservationId id() {
    return id;
  }

  public ResidentName resident() {
    return resident;
  }

  public SpaceNumber space() {
    return space;
  }

  public LocalDate date() {
    return date;
  }

  public ReservationStatus status() {
    return status;
  }
}
