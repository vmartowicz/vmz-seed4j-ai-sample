package fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain;

import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;

class FreeDayCategoryException extends RentalReservationRuleViolationException {

  FreeDayCategoryException(Category category) {
    super("Free days are only offered in ECO or COMFORT, not in " + category);
  }
}
