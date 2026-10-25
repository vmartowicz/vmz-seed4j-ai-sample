package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

import java.util.UUID;

class SponsoredTalkNotAllowedException extends CallForPapersRuleViolationException {

  SponsoredTalkNotAllowedException(UUID sponsor) {
    super("Sponsor " + sponsor + " has no sponsored talk left: only paid GOLD packs include one");
  }
}
