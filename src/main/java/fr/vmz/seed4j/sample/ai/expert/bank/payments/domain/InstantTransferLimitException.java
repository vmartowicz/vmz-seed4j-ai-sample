package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

import java.math.BigDecimal;

class InstantTransferLimitException extends PaymentsRuleViolationException {

  InstantTransferLimitException(BigDecimal amount) {
    super("Instant transfers are limited to 15000 euros, not " + amount);
  }
}
