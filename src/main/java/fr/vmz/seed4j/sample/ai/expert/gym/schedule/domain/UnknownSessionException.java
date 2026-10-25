package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

class UnknownSessionException extends UnknownScheduleElementException {

  UnknownSessionException(SessionId session) {
    super("Unknown session " + session.get());
  }
}
