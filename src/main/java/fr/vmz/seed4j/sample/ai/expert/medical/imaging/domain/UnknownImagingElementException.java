package fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain;

public abstract class UnknownImagingElementException extends RuntimeException {

  protected UnknownImagingElementException(String message) {
    super(message);
  }
}
