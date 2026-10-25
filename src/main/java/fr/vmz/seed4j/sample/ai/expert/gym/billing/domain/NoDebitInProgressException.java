package fr.vmz.seed4j.sample.ai.expert.gym.billing.domain;

class NoDebitInProgressException extends BillingRuleViolationException {

  NoDebitInProgressException(InvoiceId invoice, InvoiceStatus status) {
    super("No direct debit in progress for invoice " + invoice.get() + " which is " + status);
  }
}
