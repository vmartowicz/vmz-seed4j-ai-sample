package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.RestaurantRuleViolationException;
import java.math.BigDecimal;

class WrongDepositAmountException extends RestaurantRuleViolationException {

  WrongDepositAmountException(BigDecimal expected, BigDecimal paid) {
    super("Expected a deposit of " + expected + " euros, got " + paid);
  }
}
