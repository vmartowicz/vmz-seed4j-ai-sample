package fr.vmz.seed4j.sample.ai.expert.medical.records.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;

class UnknownPractitionerException extends UnknownMedicalRecordsElementException {

  UnknownPractitionerException(DoctorId doctor) {
    super("Unknown doctor " + doctor.get());
  }
}
