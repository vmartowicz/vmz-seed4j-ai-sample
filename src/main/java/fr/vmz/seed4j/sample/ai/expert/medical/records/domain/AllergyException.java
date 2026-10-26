package fr.vmz.seed4j.sample.ai.expert.medical.records.domain;

class AllergyException extends MedicalRecordsRuleViolationException {

  AllergyException(String medicine) {
    super("Patient is allergic to " + medicine);
  }
}
