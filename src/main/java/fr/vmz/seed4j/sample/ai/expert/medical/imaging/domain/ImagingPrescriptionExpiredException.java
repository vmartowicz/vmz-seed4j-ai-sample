package fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain;

import java.util.UUID;

class ImagingPrescriptionExpiredException extends ImagingRuleViolationException {

  ImagingPrescriptionExpiredException(UUID prescription) {
    super("Prescription " + prescription + " is more than one year old");
  }
}
