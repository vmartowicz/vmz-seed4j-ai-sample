package fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain;

public abstract class UnknownOnboardingElementException extends RuntimeException {

  protected UnknownOnboardingElementException(String message) {
    super(message);
  }
}
