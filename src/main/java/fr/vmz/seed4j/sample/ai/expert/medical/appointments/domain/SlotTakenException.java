package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

import java.time.LocalDateTime;

class SlotTakenException extends AppointmentsRuleViolationException {

  SlotTakenException(LocalDateTime start) {
    super("The slot of " + start + " is already booked");
  }
}
