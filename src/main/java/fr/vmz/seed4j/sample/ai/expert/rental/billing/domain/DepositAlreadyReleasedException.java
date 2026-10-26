package fr.vmz.seed4j.sample.ai.expert.rental.billing.domain;

import java.util.UUID;

class DepositAlreadyReleasedException extends RentalBillingRuleViolationException {

  DepositAlreadyReleasedException(UUID contract) {
    super("The deposit of contract " + contract + " is already released");
  }
}
