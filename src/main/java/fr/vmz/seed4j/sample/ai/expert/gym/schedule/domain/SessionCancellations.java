package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

public interface SessionCancellations {
  void cancelled(SessionId session);
}
