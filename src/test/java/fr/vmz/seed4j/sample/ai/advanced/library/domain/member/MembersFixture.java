package fr.vmz.seed4j.sample.ai.advanced.library.domain.member;

import java.time.LocalDate;

public final class MembersFixture {

  public static final LocalDate REGISTRATION_DAY = LocalDate.of(2026, 1, 5);

  private MembersFixture() {}

  public static Member jane() {
    return toRegister("Jane Doe", "jane@mail.com").register(REGISTRATION_DAY);
  }

  public static MemberToRegister toRegister(String name, String email) {
    return new MemberToRegister(new FullName(name), new MemberEmail(email));
  }
}
