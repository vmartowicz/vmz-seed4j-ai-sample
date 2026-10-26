package fr.vmz.seed4j.sample.ai.expert.bank.lending.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;

class UnknownBorrowerException extends UnknownLendingElementException {

  UnknownBorrowerException(CustomerId borrower) {
    super("Unknown customer " + borrower.get());
  }
}
