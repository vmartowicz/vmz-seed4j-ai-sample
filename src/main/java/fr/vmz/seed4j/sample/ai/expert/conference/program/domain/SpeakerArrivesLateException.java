package fr.vmz.seed4j.sample.ai.expert.conference.program.domain;

class SpeakerArrivesLateException extends ProgramRuleViolationException {

  SpeakerArrivesLateException(Slot slot) {
    super("Speakers coming from abroad can not speak before 11:00 on the first day, slot " + slot.number() + " starts at " + slot.start().toLocalTime());
  }
}
