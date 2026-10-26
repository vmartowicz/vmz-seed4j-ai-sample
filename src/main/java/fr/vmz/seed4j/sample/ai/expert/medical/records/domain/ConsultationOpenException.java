package fr.vmz.seed4j.sample.ai.expert.medical.records.domain;

class ConsultationOpenException extends MedicalRecordsRuleViolationException {

  ConsultationOpenException(ConsultationId consultation) {
    super("Consultation " + consultation.get() + " is still open: addenda are only for closed consultations");
  }
}
