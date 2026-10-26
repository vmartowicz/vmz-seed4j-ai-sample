package fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain;

public enum ImagingExam {
  XRAY(true),
  ULTRASOUND(false),
  CT(true),
  CT_CONTRAST(true),
  MRI(false);

  private final boolean irradiating;

  ImagingExam(boolean irradiating) {
    this.irradiating = irradiating;
  }

  public boolean irradiating() {
    return irradiating;
  }
}
