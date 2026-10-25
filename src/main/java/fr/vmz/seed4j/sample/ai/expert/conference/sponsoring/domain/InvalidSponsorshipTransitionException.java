package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain;

class InvalidSponsorshipTransitionException extends SponsoringRuleViolationException {

  InvalidSponsorshipTransitionException(SponsorId sponsorship, SponsorshipStatus status) {
    super("Sponsorship " + sponsorship.get() + " can not be paid, it is " + status);
  }
}
