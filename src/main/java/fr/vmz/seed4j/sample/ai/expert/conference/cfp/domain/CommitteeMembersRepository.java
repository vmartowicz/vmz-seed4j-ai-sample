package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

import java.util.Optional;

public interface CommitteeMembersRepository {
  void save(CommitteeMember member);

  Optional<CommitteeMember> get(CommitteeMemberId member);

  default CommitteeMember member(CommitteeMemberId member) {
    return get(member).orElseThrow(() -> new UnknownCommitteeMemberException(member));
  }
}
