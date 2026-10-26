package fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain;

class CardNotAllowedException extends RetailAccountsRuleViolationException {

  CardNotAllowedException(String reason) {
    super(reason);
  }
}
