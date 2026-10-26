package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

class MandateNotActiveException extends PaymentsRuleViolationException {

  MandateNotActiveException(MandateId mandate) {
    super("Mandate " + mandate.get() + " is revoked");
  }
}
