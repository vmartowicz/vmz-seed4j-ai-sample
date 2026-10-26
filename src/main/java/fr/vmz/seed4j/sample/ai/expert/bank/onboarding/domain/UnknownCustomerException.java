package fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;

class UnknownCustomerException extends UnknownOnboardingElementException {

  UnknownCustomerException(CustomerId customer) {
    super("Unknown customer " + customer.get());
  }
}
