package fr.vmz.seed4j.sample.ai.expert.gym.membership.domain;

import java.time.LocalDate;

class SuspensionInThePastException extends MembershipRuleViolationException {

  SuspensionInThePastException(LocalDate start) {
    super("A suspension can not start in the past: " + start);
  }
}
