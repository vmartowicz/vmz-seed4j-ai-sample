package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain;

import java.util.UUID;

class UnknownLabPrescriptionException extends UnknownLaboratoryElementException {

  UnknownLabPrescriptionException(UUID prescription) {
    super("Unknown prescription " + prescription);
  }
}
