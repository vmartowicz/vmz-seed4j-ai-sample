package fr.vmz.seed4j.sample.ai.expert.rental.counter.domain;

import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;

class NoVehicleAvailableException extends CounterRuleViolationException {

  NoVehicleAvailableException(Category category) {
    super("No vehicle available for a " + category + " booking, even with an upgrade");
  }
}
