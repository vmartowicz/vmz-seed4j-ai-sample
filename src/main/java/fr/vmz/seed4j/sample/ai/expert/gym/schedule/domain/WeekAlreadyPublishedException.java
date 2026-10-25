package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

import java.time.LocalDate;

class WeekAlreadyPublishedException extends ScheduleRuleViolationException {

  WeekAlreadyPublishedException(LocalDate monday) {
    super("The week of " + monday + " is already published");
  }
}
