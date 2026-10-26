package fr.vmz.seed4j.sample.ai.expert.medical.records.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;

class UnknownRecordPatientException extends UnknownMedicalRecordsElementException {

  UnknownRecordPatientException(PatientId patient) {
    super("Unknown patient " + patient.get());
  }
}
