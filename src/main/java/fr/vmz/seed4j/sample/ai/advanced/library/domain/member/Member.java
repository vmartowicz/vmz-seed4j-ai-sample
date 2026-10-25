package fr.vmz.seed4j.sample.ai.advanced.library.domain.member;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;

public final class Member {

  private final MemberId id;
  private final FullName name;
  private final MemberEmail email;
  private final LocalDate registeredOn;
  private MemberStatus status = MemberStatus.ACTIVE;

  Member(MemberId id, MemberToRegister member, LocalDate registeredOn) {
    Assert.notNull("registeredOn", registeredOn);

    this.id = id;
    name = member.name();
    email = member.email();
    this.registeredOn = registeredOn;
  }

  public void suspend() {
    status = MemberStatus.SUSPENDED;
  }

  public boolean isActive() {
    return status == MemberStatus.ACTIVE;
  }

  public MemberId id() {
    return id;
  }

  public FullName name() {
    return name;
  }

  public MemberEmail email() {
    return email;
  }

  public LocalDate registeredOn() {
    return registeredOn;
  }

  public MemberStatus status() {
    return status;
  }
}
