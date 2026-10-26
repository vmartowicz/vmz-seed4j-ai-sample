package fr.vmz.seed4j.sample.ai.expert.medical.records.domain;

class UnknownConsultationException extends UnknownMedicalRecordsElementException {

  UnknownConsultationException(ConsultationId consultation) {
    super("Unknown consultation " + consultation.get());
  }
}
