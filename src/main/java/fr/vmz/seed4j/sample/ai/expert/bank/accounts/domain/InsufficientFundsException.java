package fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;

public class InsufficientFundsException extends RetailAccountsRuleViolationException {

  public InsufficientFundsException(Iban iban) {
    super("Account " + iban.get() + " can not cover this operation, authorized overdraft included");
  }
}
