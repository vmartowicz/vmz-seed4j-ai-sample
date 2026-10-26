package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;

class OnlineBookingBlockedException extends AppointmentsRuleViolationException {

  OnlineBookingBlockedException(PatientId patient) {
    super("Patient " + patient.get() + " missed 3 appointments in 12 months and must book by phone");
  }
}
