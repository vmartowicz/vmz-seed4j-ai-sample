package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

class UnknownCommitteeMemberException extends UnknownCallForPapersElementException {

  UnknownCommitteeMemberException(CommitteeMemberId member) {
    super("Unknown committee member " + member.get());
  }
}
