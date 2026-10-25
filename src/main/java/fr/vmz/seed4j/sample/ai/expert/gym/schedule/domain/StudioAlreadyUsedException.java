package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

class StudioAlreadyUsedException extends ScheduleRuleViolationException {

  StudioAlreadyUsedException(Studio studio, TeachingSlot slot) {
    super("Studio " + studio.name() + " of " + studio.gym() + " is already used at " + slot.start().toLocalTime());
  }
}
