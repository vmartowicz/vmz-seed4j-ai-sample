package fr.vmz.seed4j.sample.ai.expert.rental.billing.domain;

class FinalInvoiceAlreadyPaidException extends RentalBillingRuleViolationException {

  FinalInvoiceAlreadyPaidException(FinalInvoiceId invoice) {
    super("Invoice " + invoice.get() + " is already paid");
  }
}
