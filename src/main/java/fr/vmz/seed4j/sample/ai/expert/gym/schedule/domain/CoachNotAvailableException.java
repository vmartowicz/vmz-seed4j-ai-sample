package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

class CoachNotAvailableException extends ScheduleRuleViolationException {

  CoachNotAvailableException(Coach coach, TeachingSlot slot) {
    super("Coach " + coach.name() + " is not available at " + slot.start() + " (15 minutes between classes, 30 when changing gym)");
  }
}
