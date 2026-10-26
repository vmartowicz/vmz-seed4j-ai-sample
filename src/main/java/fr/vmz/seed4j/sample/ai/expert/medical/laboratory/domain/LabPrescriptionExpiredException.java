package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain;

import java.util.UUID;

class LabPrescriptionExpiredException extends LaboratoryRuleViolationException {

  LabPrescriptionExpiredException(UUID prescription) {
    super("Prescription " + prescription + " is more than one year old");
  }
}
