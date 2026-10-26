package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

class UnknownMandateException extends UnknownPaymentsElementException {

  UnknownMandateException(MandateId mandate) {
    super("Unknown mandate " + mandate.get());
  }
}
