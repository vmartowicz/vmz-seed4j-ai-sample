package fr.vmz.seed4j.sample.ai.expert.gym.membership.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;

class InvalidMembershipTransitionException extends MembershipRuleViolationException {

  InvalidMembershipTransitionException(MemberId member, MembershipStatus status, String action) {
    super("Can not " + action + " membership " + member.get() + " which is " + status);
  }
}
