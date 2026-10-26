package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

class UnknownAppointmentException extends UnknownAppointmentsElementException {

  UnknownAppointmentException(AppointmentId appointment) {
    super("Unknown appointment " + appointment.get());
  }
}
