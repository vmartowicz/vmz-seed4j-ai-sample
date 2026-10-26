package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain;

public abstract class UnknownLaboratoryElementException extends RuntimeException {

  protected UnknownLaboratoryElementException(String message) {
    super(message);
  }
}
