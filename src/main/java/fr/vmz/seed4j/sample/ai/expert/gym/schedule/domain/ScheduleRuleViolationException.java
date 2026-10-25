package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

public abstract class ScheduleRuleViolationException extends RuntimeException {

  protected ScheduleRuleViolationException(String message) {
    super(message);
  }
}
