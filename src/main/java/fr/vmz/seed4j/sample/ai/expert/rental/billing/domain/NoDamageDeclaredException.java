package fr.vmz.seed4j.sample.ai.expert.rental.billing.domain;

import java.util.UUID;

class NoDamageDeclaredException extends RentalBillingRuleViolationException {

  NoDamageDeclaredException(UUID contract) {
    super("No damage was declared when contract " + contract + " ended");
  }
}
