package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

class InvalidProposalTransitionException extends CallForPapersRuleViolationException {

  InvalidProposalTransitionException(ProposalId proposal, ProposalStatus status, String action) {
    super("Can not " + action + " proposal " + proposal.get() + " which is " + status);
  }
}
