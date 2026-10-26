package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain;

class UnknownSampleException extends UnknownLaboratoryElementException {

  UnknownSampleException(SampleId sample) {
    super("Unknown sample " + sample.get());
  }
}
