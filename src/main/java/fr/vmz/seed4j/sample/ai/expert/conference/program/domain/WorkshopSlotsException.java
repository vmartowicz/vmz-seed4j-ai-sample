package fr.vmz.seed4j.sample.ai.expert.conference.program.domain;

class WorkshopSlotsException extends ProgramRuleViolationException {

  WorkshopSlotsException(Slot slot) {
    super("A workshop takes 2 consecutive slots of the same half day, slot " + slot.number() + " is the last one");
  }
}
