package fr.vmz.seed4j.sample.ai.expert.bank.lending.domain;

class UnknownLoanException extends UnknownLendingElementException {

  UnknownLoanException(LoanId loan) {
    super("Unknown loan " + loan.get());
  }
}
