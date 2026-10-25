package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain;

class PackSoldOutException extends SponsoringRuleViolationException {

  PackSoldOutException(Pack pack) {
    super("No " + pack + " pack left");
  }
}
