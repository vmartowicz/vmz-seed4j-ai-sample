package fr.vmz.seed4j.sample.ai.expert.conference.program.domain;

import java.util.UUID;

class UnknownConfirmedTalkException extends UnknownProgramElementException {

  UnknownConfirmedTalkException(UUID talk) {
    super("No confirmed talk " + talk);
  }
}
