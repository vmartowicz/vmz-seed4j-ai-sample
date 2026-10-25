package fr.vmz.seed4j.sample.ai.expert.conference.program.domain;

import java.time.LocalDate;

class InvalidSlotException extends InvalidProgramValueException {

  InvalidSlotException(LocalDate day, int number) {
    super("No slot " + number + " on " + day + ": the conference has 9 slots on October 15 and 16");
  }
}
