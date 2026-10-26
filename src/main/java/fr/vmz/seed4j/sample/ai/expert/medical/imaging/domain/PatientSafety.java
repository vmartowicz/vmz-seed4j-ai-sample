package fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain;

import java.time.LocalDate;
import java.time.Period;

public record PatientSafety(boolean female, LocalDate birthDate, boolean pacemaker, boolean recentCreatinine) {
  private static final int CHILDBEARING_FROM = 15;
  private static final int CHILDBEARING_TO = 50;

  boolean mayBePregnantOn(LocalDate date) {
    var age = Period.between(birthDate, date).getYears();

    return female && age >= CHILDBEARING_FROM && age <= CHILDBEARING_TO;
  }
}
