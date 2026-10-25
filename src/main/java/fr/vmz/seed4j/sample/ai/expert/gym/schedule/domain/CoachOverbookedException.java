package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

class CoachOverbookedException extends ScheduleRuleViolationException {

  CoachOverbookedException(Coach coach) {
    super("Coach " + coach.name() + " can not teach more than 25 hours a week");
  }
}
