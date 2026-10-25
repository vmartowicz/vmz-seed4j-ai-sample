package fr.vmz.seed4j.sample.ai.expert.conference.program.domain;

import java.util.UUID;

class TalkAlreadyScheduledException extends ProgramRuleViolationException {

  TalkAlreadyScheduledException(UUID talk) {
    super("Talk " + talk + " is already scheduled");
  }
}
