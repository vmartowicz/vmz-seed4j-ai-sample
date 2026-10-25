package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

class UnknownProposalException extends UnknownCallForPapersElementException {

  UnknownProposalException(ProposalId proposal) {
    super("Unknown proposal " + proposal.get());
  }
}
