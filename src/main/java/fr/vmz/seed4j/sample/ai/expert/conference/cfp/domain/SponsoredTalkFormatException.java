package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

class SponsoredTalkFormatException extends CallForPapersRuleViolationException {

  SponsoredTalkFormatException(Format format) {
    super("A sponsored talk is a TALK, not a " + format);
  }
}
