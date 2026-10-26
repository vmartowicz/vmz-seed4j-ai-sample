package fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;

class UnknownImagingPatientException extends UnknownImagingElementException {

  UnknownImagingPatientException(PatientId patient) {
    super("Unknown patient " + patient.get());
  }
}
