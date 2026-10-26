package fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public final class ImagingFixture {

  public static final LocalDateTime NOW = LocalDateTime.of(2026, 6, 1, 10, 0);
  public static final LocalDate TODAY = NOW.toLocalDate();

  private ImagingFixture() {}

  public static ImagingPrescription prescription(PatientId patient, LocalDate issuedOn, String... exams) {
    return new ImagingPrescription(UUID.randomUUID(), patient, DoctorId.newId(), issuedOn, List.of(exams));
  }

  public static PatientSafety man() {
    return new PatientSafety(false, LocalDate.of(1980, 1, 1), false, false);
  }

  public static Examination performed(ImagingExam exam) {
    var patient = PatientId.newId();
    var prescription = prescription(patient, TODAY, exam.name());

    return Examination.perform(new ExamToPerform(patient, prescription.id(), exam, false), prescription, man(), NOW);
  }
}
