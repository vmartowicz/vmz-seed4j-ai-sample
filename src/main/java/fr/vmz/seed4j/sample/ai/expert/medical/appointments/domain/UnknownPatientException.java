package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;

class UnknownPatientException extends UnknownAppointmentsElementException {

  UnknownPatientException(PatientId patient) {
    super("Unknown patient " + patient.get());
  }
}
