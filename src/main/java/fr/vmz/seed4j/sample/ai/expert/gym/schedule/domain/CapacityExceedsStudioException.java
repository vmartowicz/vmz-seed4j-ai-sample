package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

class CapacityExceedsStudioException extends ScheduleRuleViolationException {

  CapacityExceedsStudioException(Studio studio, int capacity) {
    super("Studio " + studio.name() + " of " + studio.gym() + " holds " + studio.capacity() + " people, not " + capacity);
  }
}
