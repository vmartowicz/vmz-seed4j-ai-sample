package fr.vmz.seed4j.sample.ai.expert.rental.counter.domain;

import java.util.UUID;

class UnknownBookedRentalException extends UnknownCounterElementException {

  UnknownBookedRentalException(UUID reservation) {
    super("Unknown reservation " + reservation);
  }
}
