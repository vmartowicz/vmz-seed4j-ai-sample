package fr.vmz.seed4j.sample.ai.expert.rental.billing.domain;

import java.util.UUID;

class UnknownDepositException extends UnknownRentalBillingElementException {

  UnknownDepositException(UUID contract) {
    super("No deposit for contract " + contract);
  }
}
