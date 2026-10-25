package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

public enum Ineligibility {
  MEMBERSHIP_INACTIVE("the membership is not active"),
  OTHER_GYM("Essential plans only book classes in their home gym"),
  BANNED("banned for 7 days after 3 no-shows in 30 days"),
  QUOTA_REACHED("Essential plans include 4 classes a month");

  private final String explanation;

  Ineligibility(String explanation) {
    this.explanation = explanation;
  }

  public String explanation() {
    return explanation;
  }
}
