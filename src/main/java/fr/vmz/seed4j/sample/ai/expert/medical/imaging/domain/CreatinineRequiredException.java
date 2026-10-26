package fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain;

class CreatinineRequiredException extends ImagingRuleViolationException {

  CreatinineRequiredException() {
    super("A contrast CT scan needs a validated creatinine result from the last 3 months");
  }
}
