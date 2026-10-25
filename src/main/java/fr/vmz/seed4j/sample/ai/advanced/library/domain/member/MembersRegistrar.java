package fr.vmz.seed4j.sample.ai.advanced.library.domain.member;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;

public class MembersRegistrar {

  private final MembersRepository members;

  public MembersRegistrar(MembersRepository members) {
    this.members = members;
  }

  public Member register(MemberToRegister toRegister, LocalDate today) {
    Assert.notNull("toRegister", toRegister);

    if (members.withEmail(toRegister.email()).isPresent()) {
      throw new DuplicatedMemberEmailException(toRegister.email());
    }

    var member = toRegister.register(today);
    members.save(member);

    return member;
  }
}
