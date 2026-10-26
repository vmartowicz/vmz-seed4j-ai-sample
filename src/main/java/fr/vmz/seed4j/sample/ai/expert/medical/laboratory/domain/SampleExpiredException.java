package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain;

class SampleExpiredException extends LaboratoryRuleViolationException {

  SampleExpiredException(SampleId sample) {
    super("Sample " + sample.get() + " was not analysed within 4 hours: it must be taken again");
  }
}
