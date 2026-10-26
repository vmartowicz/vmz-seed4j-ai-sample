package fr.vmz.seed4j.sample.ai.expert.bank.lending.domain;

class LoanRefusedException extends LendingRuleViolationException {

  LoanRefusedException(String reason) {
    super("Loan refused: " + reason);
  }
}
