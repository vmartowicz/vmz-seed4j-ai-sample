package fr.vmz.seed4j.sample.ai.expert.conference.cfp.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.CommitteeMember;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.CommitteeMemberToRegister;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestCommitteeMember(@Nullable UUID id, @Nullable String name, @Nullable String company) {
  static RestCommitteeMember from(CommitteeMember member) {
    return new RestCommitteeMember(member.id().get(), member.name(), member.company());
  }

  CommitteeMemberToRegister toDomain() {
    return new CommitteeMemberToRegister(name, company);
  }
}
