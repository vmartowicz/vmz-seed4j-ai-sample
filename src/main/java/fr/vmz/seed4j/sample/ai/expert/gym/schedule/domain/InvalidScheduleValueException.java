package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

public abstract class InvalidScheduleValueException extends RuntimeException {

  protected InvalidScheduleValueException(String message) {
    super(message);
  }
}
