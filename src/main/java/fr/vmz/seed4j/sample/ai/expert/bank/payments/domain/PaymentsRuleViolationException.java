package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

public abstract class PaymentsRuleViolationException extends RuntimeException {

  protected PaymentsRuleViolationException(String message) {
    super(message);
  }
}
