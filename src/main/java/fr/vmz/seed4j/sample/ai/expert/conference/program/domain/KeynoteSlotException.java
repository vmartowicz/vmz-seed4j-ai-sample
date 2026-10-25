package fr.vmz.seed4j.sample.ai.expert.conference.program.domain;

class KeynoteSlotException extends ProgramRuleViolationException {

  KeynoteSlotException(Slot slot) {
    super("The first slot of " + slot.day() + " is kept for the keynote");
  }
}
