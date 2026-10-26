package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

class UnknownPayeeException extends UnknownPaymentsElementException {

  UnknownPayeeException(PayeeId payee) {
    super("Unknown payee " + payee.get());
  }
}
