package fr.vmz.seed4j.sample.ai.intermediate.parking.domain;

import java.time.LocalDate;

public final class ReservationsFixture {

  public static final LocalDate TODAY = LocalDate.of(2026, 3, 10);

  private ReservationsFixture() {}

  public static ResidentName jean() {
    return new ResidentName("Jean Dupont");
  }

  public static Reservation reservation(int space, LocalDate date) {
    return reservation(jean(), space, date);
  }

  public static Reservation reservation(ResidentName resident, int space, LocalDate date) {
    return new ReservationToCreate(resident, new SpaceNumber(space), date).create();
  }
}
