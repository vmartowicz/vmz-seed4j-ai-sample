package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

import java.time.LocalDate;

class ConfirmationDeadlinePassedException extends CallForPapersRuleViolationException {

  ConfirmationDeadlinePassedException(ProposalId proposal, LocalDate deadline) {
    super("Proposal " + proposal.get() + " had to be confirmed by " + deadline);
  }
}
