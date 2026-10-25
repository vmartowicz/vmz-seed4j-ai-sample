package fr.vmz.seed4j.sample.ai.advanced.library.infrastructure.secondary.member;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.Member;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MemberEmail;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MemberId;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MembersRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryMembersRepository implements MembersRepository {

  private final Map<MemberId, Member> members = new ConcurrentHashMap<>();

  @Override
  public void save(Member member) {
    members.put(member.id(), member);
  }

  @Override
  public Optional<Member> get(MemberId member) {
    return Optional.ofNullable(members.get(member));
  }

  @Override
  public Optional<Member> withEmail(MemberEmail email) {
    return members
      .values()
      .stream()
      .filter(member -> member.email().equals(email))
      .findFirst();
  }
}
