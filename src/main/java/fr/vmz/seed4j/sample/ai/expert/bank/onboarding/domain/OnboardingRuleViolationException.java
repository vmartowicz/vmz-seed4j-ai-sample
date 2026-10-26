package fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain;

public abstract class OnboardingRuleViolationException extends RuntimeException {

  protected OnboardingRuleViolationException(String message) {
    super(message);
  }
}
