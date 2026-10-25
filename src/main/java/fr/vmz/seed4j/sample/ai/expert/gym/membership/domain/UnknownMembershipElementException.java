package fr.vmz.seed4j.sample.ai.expert.gym.membership.domain;

public abstract class UnknownMembershipElementException extends RuntimeException {

  protected UnknownMembershipElementException(String message) {
    super(message);
  }
}
