package fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain;

import java.util.UUID;

class UnknownImagingPrescriptionException extends UnknownImagingElementException {

  UnknownImagingPrescriptionException(UUID prescription) {
    super("Unknown prescription " + prescription);
  }
}
