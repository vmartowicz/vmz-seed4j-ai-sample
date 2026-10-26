package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

class InvalidAppointmentTransitionException extends AppointmentsRuleViolationException {

  InvalidAppointmentTransitionException(AppointmentId appointment, AppointmentStatus status, String action) {
    super("Can not " + action + " appointment " + appointment.get() + " which is " + status);
  }
}
