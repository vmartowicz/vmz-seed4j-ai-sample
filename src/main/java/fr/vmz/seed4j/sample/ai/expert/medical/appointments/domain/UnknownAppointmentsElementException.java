package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

public abstract class UnknownAppointmentsElementException extends RuntimeException {

  protected UnknownAppointmentsElementException(String message) {
    super(message);
  }
}
