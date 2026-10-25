package fr.vmz.seed4j.sample.ai.expert.gym.membership.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;

class UnknownMembershipException extends UnknownMembershipElementException {

  UnknownMembershipException(MemberId member) {
    super("Unknown membership " + member.get());
  }
}
