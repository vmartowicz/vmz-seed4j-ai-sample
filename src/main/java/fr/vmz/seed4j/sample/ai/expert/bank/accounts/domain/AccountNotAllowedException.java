package fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain;

class AccountNotAllowedException extends RetailAccountsRuleViolationException {

  AccountNotAllowedException(String reason) {
    super(reason);
  }
}
