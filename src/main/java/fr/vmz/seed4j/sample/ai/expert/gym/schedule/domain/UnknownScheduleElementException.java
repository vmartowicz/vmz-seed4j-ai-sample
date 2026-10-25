package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

public abstract class UnknownScheduleElementException extends RuntimeException {

  protected UnknownScheduleElementException(String message) {
    super(message);
  }
}
