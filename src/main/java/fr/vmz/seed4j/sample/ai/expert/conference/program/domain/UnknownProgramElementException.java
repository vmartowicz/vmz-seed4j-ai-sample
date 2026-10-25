package fr.vmz.seed4j.sample.ai.expert.conference.program.domain;

public abstract class UnknownProgramElementException extends RuntimeException {

  protected UnknownProgramElementException(String message) {
    super(message);
  }
}
