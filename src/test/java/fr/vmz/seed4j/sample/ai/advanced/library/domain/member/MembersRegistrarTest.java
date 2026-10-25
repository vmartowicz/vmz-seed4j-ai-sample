package fr.vmz.seed4j.sample.ai.advanced.library.domain.member;

import static fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MembersFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.util.Optional;
import org.junit.jupiter.api.Test;

@UnitTest
class MembersRegistrarTest {

  private final MembersRepository members = mock(MembersRepository.class);
  private final MembersRegistrar registrar = new MembersRegistrar(members);

  @Test
  void shouldRegisterMember() {
    when(members.withEmail(any())).thenReturn(Optional.empty());

    var member = registrar.register(toRegister("Jane Doe", "jane@mail.com"), REGISTRATION_DAY);

    assertThat(member.registeredOn()).isEqualTo(REGISTRATION_DAY);
    verify(members).save(member);
  }

  @Test
  void shouldNotRegisterTwoMembersWithTheSameEmail() {
    when(members.withEmail(new MemberEmail("jane@mail.com"))).thenReturn(Optional.of(jane()));

    assertThatThrownBy(() -> registrar.register(toRegister("Jane Smith", "JANE@mail.com"), REGISTRATION_DAY))
      .isExactlyInstanceOf(DuplicatedMemberEmailException.class)
      .hasMessageContaining("jane@mail.com");
    verify(members, never()).save(any());
  }
}
