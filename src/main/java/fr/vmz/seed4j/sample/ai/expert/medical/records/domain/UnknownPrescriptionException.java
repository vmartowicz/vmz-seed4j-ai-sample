package fr.vmz.seed4j.sample.ai.expert.medical.records.domain;

class UnknownPrescriptionException extends UnknownMedicalRecordsElementException {

  UnknownPrescriptionException(PrescriptionId prescription) {
    super("Unknown prescription " + prescription.get());
  }
}
