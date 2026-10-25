package fr.vmz.seed4j.sample.ai.expert.gym.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;

class NotInArrearsException extends BillingRuleViolationException {

  NotInArrearsException(MemberId member) {
    super("Member " + member.get() + " has no arrears to settle");
  }
}
