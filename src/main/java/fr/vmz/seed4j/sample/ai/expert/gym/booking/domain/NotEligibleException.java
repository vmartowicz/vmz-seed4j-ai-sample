package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;

class NotEligibleException extends ClassBookingRuleViolationException {

  NotEligibleException(MemberId member, Ineligibility reason) {
    super("Member " + member.get() + " can not book: " + reason.explanation());
  }
}
