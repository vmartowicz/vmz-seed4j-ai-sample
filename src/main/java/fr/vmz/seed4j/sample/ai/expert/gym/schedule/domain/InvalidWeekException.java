package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

import java.time.LocalDate;

class InvalidWeekException extends InvalidScheduleValueException {

  InvalidWeekException(LocalDate date) {
    super("A week starts on a Monday, not on " + date.getDayOfWeek());
  }
}
