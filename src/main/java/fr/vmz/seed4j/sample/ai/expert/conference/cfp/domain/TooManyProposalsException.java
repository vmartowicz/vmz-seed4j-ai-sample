package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;

class TooManyProposalsException extends CallForPapersRuleViolationException {

  TooManyProposalsException(Email speaker) {
    super("Speaker " + speaker.get() + " already submitted 3 proposals");
  }
}
