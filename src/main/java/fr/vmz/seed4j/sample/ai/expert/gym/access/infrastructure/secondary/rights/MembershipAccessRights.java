package fr.vmz.seed4j.sample.ai.expert.gym.access.infrastructure.secondary.rights;

import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.AccessRights;
import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.AccessRightsProvider;
import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.MemberStatus;
import fr.vmz.seed4j.sample.ai.expert.gym.billing.infrastructure.primary.JavaBilling;
import fr.vmz.seed4j.sample.ai.expert.gym.membership.infrastructure.primary.JavaMembership;
import fr.vmz.seed4j.sample.ai.expert.gym.membership.infrastructure.primary.JavaMemberships;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.util.Set;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
class MembershipAccessRights implements AccessRightsProvider {

  private static final AccessRights NO_RIGHTS = new AccessRights(MemberStatus.INACTIVE, false, Set.of());

  private final JavaMemberships memberships;
  private final JavaBilling billing;

  // Lazy: memberships issue badges, the contexts depend on each other
  MembershipAccessRights(@Lazy JavaMemberships memberships, @Lazy JavaBilling billing) {
    this.memberships = memberships;
    this.billing = billing;
  }

  @Override
  public AccessRights rightsOf(MemberId member) {
    return memberships
      .membership(member.get())
      .map(membership -> new AccessRights(status(membership), billing.isInArrears(member.get()), membership.gyms()))
      .orElse(NO_RIGHTS);
  }

  private static MemberStatus status(JavaMembership membership) {
    if (membership.suspended()) {
      return MemberStatus.SUSPENDED;
    }

    return membership.active() ? MemberStatus.ACTIVE : MemberStatus.INACTIVE;
  }
}
