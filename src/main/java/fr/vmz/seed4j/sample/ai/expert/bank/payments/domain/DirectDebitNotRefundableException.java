package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

class DirectDebitNotRefundableException extends PaymentsRuleViolationException {

  DirectDebitNotRefundableException(DirectDebitId debit) {
    super("Direct debit " + debit.get() + " can only be refunded within 8 weeks after its collection");
  }
}
