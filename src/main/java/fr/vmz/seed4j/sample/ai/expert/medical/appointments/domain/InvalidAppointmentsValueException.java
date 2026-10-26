package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

public abstract class InvalidAppointmentsValueException extends RuntimeException {

  protected InvalidAppointmentsValueException(String message) {
    super(message);
  }
}
