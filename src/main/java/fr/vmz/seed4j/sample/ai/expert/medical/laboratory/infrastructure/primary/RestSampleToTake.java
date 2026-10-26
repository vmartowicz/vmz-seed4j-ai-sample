package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.LabAnalysis;
import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.SampleToTake;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestSampleToTake(@Nullable UUID patientId, @Nullable UUID prescriptionId, @Nullable LabAnalysis test, @Nullable Boolean fasting) {
  SampleToTake toDomain() {
    return new SampleToTake(new PatientId(patientId), prescriptionId, test, Boolean.TRUE.equals(fasting));
  }
}
