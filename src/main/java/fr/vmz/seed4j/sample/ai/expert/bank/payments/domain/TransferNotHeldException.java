package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

class TransferNotHeldException extends PaymentsRuleViolationException {

  TransferNotHeldException(TransferId transfer, TransferStatus status) {
    super("Transfer " + transfer.get() + " is " + status + ", not held");
  }
}
