package fr.vmz.seed4j.sample.ai.expert.conference.cfp.infrastructure.secondary.committee;

import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.CommitteeMember;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.CommitteeMemberId;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.CommitteeMembersRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryCommitteeMembersRepository implements CommitteeMembersRepository {

  private final Map<CommitteeMemberId, CommitteeMember> members = new ConcurrentHashMap<>();

  @Override
  public void save(CommitteeMember member) {
    members.put(member.id(), member);
  }

  @Override
  public Optional<CommitteeMember> get(CommitteeMemberId member) {
    return Optional.ofNullable(members.get(member));
  }
}
