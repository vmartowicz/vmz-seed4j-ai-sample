package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

class UnknownDirectDebitException extends UnknownPaymentsElementException {

  UnknownDirectDebitException(DirectDebitId debit) {
    super("Unknown direct debit " + debit.get());
  }
}
