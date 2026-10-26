package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.util.UUID;

public record SampleToTake(PatientId patient, UUID prescription, LabAnalysis test, boolean fasting) {
  public SampleToTake {
    Assert.notNull("patient", patient);
    Assert.notNull("prescription", prescription);
    Assert.notNull("test", test);
  }
}
