package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain;

class UnknownSponsorshipException extends UnknownSponsoringElementException {

  UnknownSponsorshipException(SponsorId sponsorship) {
    super("Unknown sponsorship " + sponsorship.get());
  }
}
