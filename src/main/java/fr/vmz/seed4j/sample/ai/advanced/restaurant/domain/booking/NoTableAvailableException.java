package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.RestaurantRuleViolationException;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table.PartySize;

class NoTableAvailableException extends RestaurantRuleViolationException {

  NoTableAvailableException(Slot slot, PartySize party) {
    super("No table available for " + party.get() + " people at " + slot.at());
  }
}
