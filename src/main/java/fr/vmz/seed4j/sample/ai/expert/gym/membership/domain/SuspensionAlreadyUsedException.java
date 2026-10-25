package fr.vmz.seed4j.sample.ai.expert.gym.membership.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.time.Year;

class SuspensionAlreadyUsedException extends MembershipRuleViolationException {

  SuspensionAlreadyUsedException(MemberId member, Year year) {
    super("Membership " + member.get() + " was already suspended in " + year);
  }
}
