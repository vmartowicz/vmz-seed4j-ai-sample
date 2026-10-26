package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain;

class FastingRequiredException extends LaboratoryRuleViolationException {

  FastingRequiredException(LabAnalysis test) {
    super("Patient must be fasting for " + test);
  }
}
