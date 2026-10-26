package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

class UnknownTransferException extends UnknownPaymentsElementException {

  UnknownTransferException(TransferId transfer) {
    super("Unknown transfer " + transfer.get());
  }
}
