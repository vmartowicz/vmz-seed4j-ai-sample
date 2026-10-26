package fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain;

import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;

class YoungDriverCategoryException extends RentalReservationRuleViolationException {

  YoungDriverCategoryException(Category category) {
    super("Drivers under 25 can not rent a " + category);
  }
}
