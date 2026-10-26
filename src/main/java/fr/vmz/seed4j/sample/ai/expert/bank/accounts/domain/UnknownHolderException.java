package fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;

class UnknownHolderException extends UnknownRetailAccountsElementException {

  UnknownHolderException(CustomerId customer) {
    super("Unknown customer " + customer.get());
  }
}
