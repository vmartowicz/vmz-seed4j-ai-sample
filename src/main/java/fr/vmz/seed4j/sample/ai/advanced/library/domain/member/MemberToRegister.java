package fr.vmz.seed4j.sample.ai.advanced.library.domain.member;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;

public record MemberToRegister(FullName name, MemberEmail email) {
  public MemberToRegister {
    Assert.notNull("name", name);
    Assert.notNull("email", email);
  }

  public Member register(LocalDate today) {
    return new Member(MemberId.newId(), this, today);
  }
}
