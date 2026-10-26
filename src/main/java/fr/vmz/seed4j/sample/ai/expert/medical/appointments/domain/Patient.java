package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

public final class Patient {

  private static final int COMPANION_AGE = 16;

  private final PatientId id;
  private final String lastName;
  private final String firstName;
  private final LocalDate birthDate;
  private final Sex sex;
  private final SocialSecurityNumber socialSecurityNumber;
  private final String phone;
  private final boolean longTermIllness;
  private final boolean solidarityHealthCover;
  private @Nullable DoctorId referringDoctor;
  private PatientId insured;

  Patient(PatientToRegister patient) {
    id = PatientId.newId();
    lastName = patient.lastName().trim();
    firstName = patient.firstName().trim();
    birthDate = patient.birthDate();
    sex = patient.sex();
    socialSecurityNumber = new SocialSecurityNumber(patient.socialSecurityNumber());
    phone = patient.phone().trim();
    longTermIllness = patient.longTermIllness();
    solidarityHealthCover = patient.solidarityHealthCover();
    referringDoctor = patient.referringDoctor();
    insured = patient.insured() == null ? id : patient.insured();
  }

  public void changeReferringDoctor(DoctorId doctor) {
    referringDoctor = doctor;
  }

  public void attachTo(PatientId insuredPatient) {
    insured = insuredPatient;
  }

  public boolean isUnder16On(LocalDate date) {
    return Period.between(birthDate, date).getYears() < COMPANION_AGE;
  }

  public PatientId id() {
    return id;
  }

  public String lastName() {
    return lastName;
  }

  public String firstName() {
    return firstName;
  }

  public LocalDate birthDate() {
    return birthDate;
  }

  public Sex sex() {
    return sex;
  }

  public SocialSecurityNumber socialSecurityNumber() {
    return socialSecurityNumber;
  }

  public String phone() {
    return phone;
  }

  public Optional<DoctorId> referringDoctor() {
    return Optional.ofNullable(referringDoctor);
  }

  public PatientId insured() {
    return insured;
  }

  public boolean longTermIllness() {
    return longTermIllness;
  }

  public boolean solidarityHealthCover() {
    return solidarityHealthCover;
  }
}
