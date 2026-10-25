package fr.vmz.seed4j.sample.ai.expert.conference.program.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;

class SpeakerBusyException extends ProgramRuleViolationException {

  SpeakerBusyException(Email speaker, Slot slot) {
    super("Speaker " + speaker.get() + " already speaks at " + slot.start());
  }
}
