package fr.vmz.seed4j.sample.ai.expert.gym.billing.domain;

class InvoiceAlreadyPaidException extends BillingRuleViolationException {

  InvoiceAlreadyPaidException(InvoiceId invoice) {
    super("Invoice " + invoice.get() + " is already paid");
  }
}
