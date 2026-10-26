package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

import java.time.LocalDateTime;

class NotASlotException extends AppointmentsRuleViolationException {

  NotASlotException(LocalDateTime start) {
    super("No bookable slot starts at " + start);
  }
}
