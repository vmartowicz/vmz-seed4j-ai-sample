package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;

class DuplicatedSpeakerException extends CallForPapersRuleViolationException {

  DuplicatedSpeakerException(Email speaker) {
    super("Speaker " + speaker.get() + " is listed twice");
  }
}
