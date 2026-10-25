package fr.vmz.seed4j.sample.ai.advanced.library.domain.member;

import static fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MembersFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import org.junit.jupiter.api.Test;

@UnitTest
class MemberTest {

  @Test
  void shouldRegisterActiveMemberToday() {
    var member = jane();

    assertThat(member.id()).isNotNull();
    assertThat(member.name()).isEqualTo(new FullName("Jane Doe"));
    assertThat(member.email()).isEqualTo(new MemberEmail("jane@mail.com"));
    assertThat(member.registeredOn()).isEqualTo(REGISTRATION_DAY);
    assertThat(member.status()).isEqualTo(MemberStatus.ACTIVE);
    assertThat(member.isActive()).isTrue();
  }

  @Test
  void shouldSuspendMember() {
    var member = jane();

    member.suspend();

    assertThat(member.status()).isEqualTo(MemberStatus.SUSPENDED);
    assertThat(member.isActive()).isFalse();
  }
}
