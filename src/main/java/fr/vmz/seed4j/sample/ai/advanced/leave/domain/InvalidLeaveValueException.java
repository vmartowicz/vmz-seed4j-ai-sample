package fr.vmz.seed4j.sample.ai.advanced.leave.domain;

public abstract class InvalidLeaveValueException extends RuntimeException {

  protected InvalidLeaveValueException(String message) {
    super(message);
  }
}
