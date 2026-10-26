package fr.vmz.seed4j.sample.ai.expert.medical.records.domain;

class ConsultationClosedException extends MedicalRecordsRuleViolationException {

  ConsultationClosedException(ConsultationId consultation) {
    super("Consultation " + consultation.get() + " is closed: add a signed addendum instead");
  }
}
