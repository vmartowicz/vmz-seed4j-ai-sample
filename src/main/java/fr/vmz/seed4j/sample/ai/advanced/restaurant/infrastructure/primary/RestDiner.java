package fr.vmz.seed4j.sample.ai.advanced.restaurant.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.Diner;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.DinerStatus;
import java.time.Year;

record RestDiner(String email, DinerStatus status, int honoredBookings, int noShows, long cancellationsThisYear) {
  static RestDiner from(Diner diner, Year year) {
    return new RestDiner(diner.email().get(), diner.status(), diner.honoredBookings(), diner.noShows(), diner.cancellationsIn(year));
  }
}
