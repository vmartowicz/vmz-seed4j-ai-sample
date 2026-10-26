package fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain;

class IncompleteFileException extends OnboardingRuleViolationException {

  IncompleteFileException(String reason) {
    super("Incomplete file: " + reason);
  }
}
