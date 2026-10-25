package fr.vmz.seed4j.sample.ai.advanced.library.domain.loan;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.LibraryRuleViolationException;

class LoanAlreadyReturnedException extends LibraryRuleViolationException {

  LoanAlreadyReturnedException(LoanId loan) {
    super("Loan " + loan.get() + " is already returned");
  }
}
