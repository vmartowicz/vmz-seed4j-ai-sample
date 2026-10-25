package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

class UnknownCoachException extends UnknownScheduleElementException {

  UnknownCoachException(CoachId coach) {
    super("Unknown coach " + coach.get());
  }
}
