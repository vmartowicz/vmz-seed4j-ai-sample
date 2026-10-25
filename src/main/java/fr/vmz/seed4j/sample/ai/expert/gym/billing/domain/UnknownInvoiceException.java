package fr.vmz.seed4j.sample.ai.expert.gym.billing.domain;

class UnknownInvoiceException extends UnknownBillingElementException {

  UnknownInvoiceException(InvoiceId invoice) {
    super("Unknown invoice " + invoice.get());
  }
}
