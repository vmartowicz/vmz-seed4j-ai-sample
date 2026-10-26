package fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.util.UUID;

public record ExamToPerform(PatientId patient, UUID prescription, ImagingExam exam, boolean negativePregnancyTest) {
  public ExamToPerform {
    Assert.notNull("patient", patient);
    Assert.notNull("prescription", prescription);
    Assert.notNull("exam", exam);
  }
}
