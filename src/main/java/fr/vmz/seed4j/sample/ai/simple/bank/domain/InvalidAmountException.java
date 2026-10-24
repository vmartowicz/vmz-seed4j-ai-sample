package fr.vmz.seed4j.sample.ai.simple.bank.domain;

import java.math.BigDecimal;

class InvalidAmountException extends InvalidBankValueException {

  InvalidAmountException(BigDecimal amount) {
    super("An amount has at most 2 decimals, got: " + amount.toPlainString());
  }
}
