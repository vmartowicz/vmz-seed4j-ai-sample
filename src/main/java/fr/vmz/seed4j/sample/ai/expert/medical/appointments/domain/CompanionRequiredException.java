package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;

class CompanionRequiredException extends AppointmentsRuleViolationException {

  CompanionRequiredException(PatientId patient) {
    super("Patient " + patient.get() + " is under 16 and must come with a companion");
  }
}
