package fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain;

class PregnancyTestRequiredException extends ImagingRuleViolationException {

  PregnancyTestRequiredException(ImagingExam exam) {
    super("A negative pregnancy test is required before " + exam);
  }
}
