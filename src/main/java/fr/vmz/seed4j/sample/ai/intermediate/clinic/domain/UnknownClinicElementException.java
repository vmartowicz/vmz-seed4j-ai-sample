package fr.vmz.seed4j.sample.ai.intermediate.clinic.domain;

public abstract class UnknownClinicElementException extends RuntimeException {

  protected UnknownClinicElementException(String message) {
    super(message);
  }
}
