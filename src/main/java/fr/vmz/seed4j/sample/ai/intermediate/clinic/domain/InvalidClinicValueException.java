package fr.vmz.seed4j.sample.ai.intermediate.clinic.domain;

public abstract class InvalidClinicValueException extends RuntimeException {

  protected InvalidClinicValueException(String message) {
    super(message);
  }
}
