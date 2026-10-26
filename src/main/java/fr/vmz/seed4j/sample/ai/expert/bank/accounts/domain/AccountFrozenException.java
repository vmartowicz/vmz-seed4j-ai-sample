package fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;

public class AccountFrozenException extends RetailAccountsRuleViolationException {

  public AccountFrozenException(Iban iban) {
    super("Account " + iban.get() + " is frozen");
  }
}
