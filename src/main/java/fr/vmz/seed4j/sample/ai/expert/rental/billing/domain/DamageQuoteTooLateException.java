package fr.vmz.seed4j.sample.ai.expert.rental.billing.domain;

import java.util.UUID;

class DamageQuoteTooLateException extends RentalBillingRuleViolationException {

  DamageQuoteTooLateException(UUID contract) {
    super("Damage quotes are accepted up to 30 days after the return of contract " + contract);
  }
}
