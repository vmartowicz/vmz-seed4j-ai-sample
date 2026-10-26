package fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain;

import java.util.UUID;

class ExamNotPrescribedException extends ImagingRuleViolationException {

  ExamNotPrescribedException(UUID prescription, ImagingExam exam) {
    super("Prescription " + prescription + " does not prescribe " + exam + " for this patient");
  }
}
