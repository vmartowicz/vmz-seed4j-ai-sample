package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;

class AlreadyBookedException extends ClassBookingRuleViolationException {

  AlreadyBookedException(MemberId member, SessionId session) {
    super("Member " + member.get() + " already booked session " + session.get());
  }
}
