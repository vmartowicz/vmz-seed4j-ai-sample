package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain;

import java.util.UUID;

class TestNotPrescribedException extends LaboratoryRuleViolationException {

  TestNotPrescribedException(UUID prescription, LabAnalysis test) {
    super("Prescription " + prescription + " does not prescribe " + test + " for this patient");
  }
}
