package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;

class UnknownClassBookingMemberException extends UnknownClassBookingElementException {

  UnknownClassBookingMemberException(MemberId member) {
    super("Unknown member " + member.get());
  }
}
