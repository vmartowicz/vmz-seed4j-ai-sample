package fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ImagingPrescription(UUID id, PatientId patient, DoctorId prescriber, LocalDate issuedOn, List<String> exams) {
  private static final int VALIDITY_YEARS = 1;

  boolean prescribes(PatientId otherPatient, ImagingExam exam) {
    return patient.equals(otherPatient) && exams.stream().anyMatch(exam.name()::equalsIgnoreCase);
  }

  boolean isExpiredOn(LocalDate date) {
    return date.isAfter(issuedOn.plusYears(VALIDITY_YEARS));
  }
}
