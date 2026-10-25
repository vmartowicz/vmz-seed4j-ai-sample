package fr.vmz.seed4j.sample.ai.expert.conference.program.domain;

public abstract class InvalidProgramValueException extends RuntimeException {

  protected InvalidProgramValueException(String message) {
    super(message);
  }
}
