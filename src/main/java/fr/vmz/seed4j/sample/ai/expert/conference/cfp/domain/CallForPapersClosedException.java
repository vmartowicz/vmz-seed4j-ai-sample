package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

import java.time.LocalDate;

class CallForPapersClosedException extends CallForPapersRuleViolationException {

  CallForPapersClosedException(LocalDate date) {
    super("The call for papers is open from February 1st to March 31st, not on " + date);
  }
}
