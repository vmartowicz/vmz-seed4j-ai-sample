package fr.vmz.seed4j.sample.ai.expert.gym.membership.domain;

class ParentalConsentRequiredException extends MembershipRuleViolationException {

  ParentalConsentRequiredException(int age) {
    super("A parental consent is required at " + age);
  }
}
