package fr.vmz.seed4j.sample.ai.expert.rental.counter.domain;

import java.util.Optional;
import java.util.UUID;

public interface Reservations {
  Optional<BookedRental> reservation(UUID reservation);

  void markPickedUp(UUID reservation);
}
