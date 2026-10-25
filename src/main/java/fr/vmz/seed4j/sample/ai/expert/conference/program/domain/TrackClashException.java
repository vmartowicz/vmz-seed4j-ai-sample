package fr.vmz.seed4j.sample.ai.expert.conference.program.domain;

class TrackClashException extends ProgramRuleViolationException {

  TrackClashException(Track track, Slot slot) {
    super("Another " + track + " session is already scheduled at " + slot.start());
  }
}
