package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

class ConflictOfInterestException extends CallForPapersRuleViolationException {

  ConflictOfInterestException(CommitteeMember member) {
    super("Committee member " + member.name() + " can not rate a proposal from a speaker of the same company");
  }
}
