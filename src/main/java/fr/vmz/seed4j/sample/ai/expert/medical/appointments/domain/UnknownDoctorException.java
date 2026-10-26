package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;

class UnknownDoctorException extends UnknownAppointmentsElementException {

  UnknownDoctorException(DoctorId doctor) {
    super("Unknown doctor " + doctor.get());
  }
}
