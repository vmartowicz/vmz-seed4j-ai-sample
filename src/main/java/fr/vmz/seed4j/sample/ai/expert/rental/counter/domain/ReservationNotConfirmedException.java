package fr.vmz.seed4j.sample.ai.expert.rental.counter.domain;

import java.util.UUID;

class ReservationNotConfirmedException extends CounterRuleViolationException {

  ReservationNotConfirmedException(UUID reservation) {
    super("Reservation " + reservation + " is not waiting for a pick-up");
  }
}
