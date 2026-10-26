package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;

class UnknownPayerAccountException extends UnknownPaymentsElementException {

  UnknownPayerAccountException(Iban account) {
    super("Unknown account " + account.get());
  }
}
