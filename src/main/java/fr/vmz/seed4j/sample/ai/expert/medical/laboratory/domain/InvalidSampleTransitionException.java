package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain;

class InvalidSampleTransitionException extends LaboratoryRuleViolationException {

  InvalidSampleTransitionException(SampleId sample, SampleStatus status, String action) {
    super("Sample " + sample.get() + " is " + status + ": cannot " + action);
  }
}
