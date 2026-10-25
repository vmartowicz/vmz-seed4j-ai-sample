package fr.vmz.seed4j.sample.ai.advanced.library.domain.member;

import java.util.Optional;

public interface MembersRepository {
  void save(Member member);

  Optional<Member> get(MemberId member);

  Optional<Member> withEmail(MemberEmail email);

  default Member member(MemberId member) {
    return get(member).orElseThrow(() -> new UnknownMemberException(member));
  }
}
