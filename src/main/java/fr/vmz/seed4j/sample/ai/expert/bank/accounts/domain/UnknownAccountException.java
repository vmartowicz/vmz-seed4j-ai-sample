package fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;

class UnknownAccountException extends UnknownRetailAccountsElementException {

  UnknownAccountException(Iban iban) {
    super("Unknown account " + iban.get());
  }
}
