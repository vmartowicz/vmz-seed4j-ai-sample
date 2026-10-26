package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

class CancellationTooLateException extends AppointmentsRuleViolationException {

  CancellationTooLateException(AppointmentId appointment) {
    super("Appointment " + appointment.get() + " can only be cancelled up to 24 hours before");
  }
}
