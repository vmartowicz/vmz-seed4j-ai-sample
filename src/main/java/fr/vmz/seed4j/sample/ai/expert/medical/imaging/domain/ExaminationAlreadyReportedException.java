package fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain;

class ExaminationAlreadyReportedException extends ImagingRuleViolationException {

  ExaminationAlreadyReportedException(ExamId examination) {
    super("Examination " + examination.get() + " is already reported");
  }
}
