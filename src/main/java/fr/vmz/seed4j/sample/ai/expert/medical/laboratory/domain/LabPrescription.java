package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record LabPrescription(UUID id, PatientId patient, DoctorId prescriber, LocalDate issuedOn, List<String> tests) {
  private static final int VALIDITY_YEARS = 1;

  boolean prescribes(PatientId otherPatient, LabAnalysis test) {
    return patient.equals(otherPatient) && tests.stream().anyMatch(test.name()::equalsIgnoreCase);
  }

  boolean isExpiredOn(LocalDate date) {
    return date.isAfter(issuedOn.plusYears(VALIDITY_YEARS));
  }
}
