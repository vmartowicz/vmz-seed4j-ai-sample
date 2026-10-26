package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.LabSample;
import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.LabAnalysis;
import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.SampleStatus;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestLabSample(
  UUID id,
  UUID patientId,
  UUID prescriptionId,
  LabAnalysis test,
  LocalDateTime takenAt,
  SampleStatus status,
  @Nullable BigDecimal value,
  boolean abnormal,
  boolean critical,
  @Nullable String validatedBy,
  @Nullable LocalDateTime validatedAt,
  @Nullable UUID alertedDoctorId,
  @Nullable UUID announcedBy
) {
  static RestLabSample from(LabSample sample) {
    return new RestLabSample(
      sample.id().get(),
      sample.patient().get(),
      sample.prescription(),
      sample.test(),
      sample.takenAt(),
      sample.status(),
      sample.value().orElse(null),
      sample.isAbnormal(),
      sample.isCritical(),
      sample.validatedBy().orElse(null),
      sample.validatedAt().orElse(null),
      sample.alertedDoctor().map(DoctorId::get).orElse(null),
      sample.announcedBy().map(DoctorId::get).orElse(null)
    );
  }
}
