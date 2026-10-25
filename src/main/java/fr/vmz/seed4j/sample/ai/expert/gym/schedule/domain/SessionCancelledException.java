package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

class SessionCancelledException extends ScheduleRuleViolationException {

  SessionCancelledException(SessionId session) {
    super("Session " + session.get() + " is cancelled");
  }
}
