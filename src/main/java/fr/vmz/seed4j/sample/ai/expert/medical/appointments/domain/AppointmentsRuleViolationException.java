package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

public abstract class AppointmentsRuleViolationException extends RuntimeException {

  protected AppointmentsRuleViolationException(String message) {
    super(message);
  }
}
