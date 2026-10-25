package fr.vmz.seed4j.sample.ai.expert.gym.access.domain;

public enum RefusalReason {
  MEMBERSHIP_INACTIVE("Please go to the front desk"),
  SUSPENDED("Please go to the front desk"),
  IN_ARREARS("Please go to the front desk"),
  GYM_NOT_INCLUDED("Your plan does not include this gym"),
  ALREADY_INSIDE("This badge is already inside"),
  GYM_FULL("The gym is full, please come back later");

  private final String message;

  RefusalReason(String message) {
    this.message = message;
  }

  public String message() {
    return message;
  }
}
