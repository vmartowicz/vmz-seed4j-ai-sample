package fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain;

class UnknownExaminationException extends UnknownImagingElementException {

  UnknownExaminationException(ExamId examination) {
    super("Unknown examination " + examination.get());
  }
}
