package fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain;

class PacemakerException extends ImagingRuleViolationException {

  PacemakerException() {
    super("MRI is forbidden to pacemaker wearers");
  }
}
