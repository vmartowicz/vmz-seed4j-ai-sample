package fr.vmz.seed4j.sample.ai.expert.medical.billing.domain;

class UnknownBillingPartyException extends UnknownMedicalBillingElementException {

  UnknownBillingPartyException(String party) {
    super("Unknown " + party);
  }
}
