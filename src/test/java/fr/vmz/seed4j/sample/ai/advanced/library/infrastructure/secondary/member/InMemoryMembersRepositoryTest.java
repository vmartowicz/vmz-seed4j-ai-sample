package fr.vmz.seed4j.sample.ai.advanced.library.infrastructure.secondary.member;

import static fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MembersFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MemberEmail;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MemberId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryMembersRepositoryTest {

  private final InMemoryMembersRepository members = new InMemoryMembersRepository();

  @Test
  void shouldGetSavedMember() {
    var member = jane();
    members.save(member);

    assertThat(members.get(member.id())).contains(member);
    assertThat(members.get(MemberId.newId())).isEmpty();
  }

  @Test
  void shouldFindMemberByEmail() {
    var member = jane();
    members.save(member);

    assertThat(members.withEmail(new MemberEmail("JANE@mail.com"))).contains(member);
    assertThat(members.withEmail(new MemberEmail("john@mail.com"))).isEmpty();
  }
}
