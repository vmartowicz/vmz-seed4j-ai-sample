package fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;

class SavingsCeilingException extends RetailAccountsRuleViolationException {

  SavingsCeilingException(Iban iban) {
    super("A Livret A can not exceed 22950 euros: " + iban.get());
  }
}
