package fr.vmz.seed4j.sample.ai.advanced.library.domain.loan;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.UnknownLibraryElementException;

class UnknownLoanException extends UnknownLibraryElementException {

  UnknownLoanException(LoanId loan) {
    super("Unknown loan " + loan.get());
  }
}
