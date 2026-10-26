package fr.vmz.seed4j.sample.ai.expert.rental.billing.domain;

class UnknownFinalInvoiceException extends UnknownRentalBillingElementException {

  UnknownFinalInvoiceException(FinalInvoiceId invoice) {
    super("Unknown invoice " + invoice.get());
  }
}
