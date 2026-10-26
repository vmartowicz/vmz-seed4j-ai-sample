package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public final class LabFixture {

  public static final LocalDateTime NOW = LocalDateTime.of(2026, 6, 1, 10, 0);

  private LabFixture() {}

  public static LabPrescription prescription(PatientId patient, LocalDate issuedOn, String... tests) {
    return new LabPrescription(UUID.randomUUID(), patient, DoctorId.newId(), issuedOn, List.of(tests));
  }

  public static LabSample taken(LabAnalysis test) {
    var patient = PatientId.newId();

    return LabSample.take(new SampleToTake(patient, UUID.randomUUID(), test, true), prescription(patient, NOW.toLocalDate(), test.name()), NOW);
  }
}
