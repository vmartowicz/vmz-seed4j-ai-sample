package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

import java.time.LocalDateTime;

class EmergencySlotException extends AppointmentsRuleViolationException {

  EmergencySlotException(LocalDateTime start) {
    super("The slot of " + start + " is kept for same-day emergencies booked by phone");
  }
}
