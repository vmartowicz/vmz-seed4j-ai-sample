package fr.vmz.seed4j.sample.ai.expert.gym.membership.domain;

class MemberTooYoungException extends MembershipRuleViolationException {

  MemberTooYoungException(int age) {
    super("Members must be at least 16, not " + age);
  }
}
