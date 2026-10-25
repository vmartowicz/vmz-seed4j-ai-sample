package fr.vmz.seed4j.sample.ai.expert.gym.membership.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import java.time.LocalDate;

public final class MembershipsFixture {

  public static final LocalDate TODAY = LocalDate.of(2026, 3, 10);

  private MembershipsFixture() {}

  public static MembershipToSubscribe essential() {
    return new MembershipToSubscribe("Jane Doe", LocalDate.of(1990, 5, 4), false, false, Plan.ESSENTIAL, true, Gym.CONFLUENCE);
  }

  public static MembershipToSubscribe premiumWithoutCommitment() {
    return new MembershipToSubscribe("John Doe", LocalDate.of(1985, 1, 20), false, false, Plan.PREMIUM, false, Gym.PART_DIEU);
  }

  public static MembershipToSubscribe born(LocalDate birthDate, boolean parentalConsent) {
    return new MembershipToSubscribe("Young Doe", birthDate, parentalConsent, false, Plan.ESSENTIAL, true, Gym.ECULLY);
  }

  public static Membership activeMembership() {
    var membership = essential().subscribe(TODAY);
    membership.activate();

    return membership;
  }
}
