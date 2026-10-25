package fr.vmz.seed4j.sample.ai.expert.gym.booking.infrastructure.secondary.membership;

import fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.MemberPlan;
import fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.MemberPlans;
import fr.vmz.seed4j.sample.ai.expert.gym.membership.infrastructure.primary.JavaMemberships;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.util.Optional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
class MembershipMemberPlans implements MemberPlans {

  private final JavaMemberships memberships;

  MembershipMemberPlans(@Lazy JavaMemberships memberships) {
    this.memberships = memberships;
  }

  @Override
  public Optional<MemberPlan> planOf(MemberId member) {
    return memberships
      .membership(member.get())
      .map(membership -> new MemberPlan(membership.active(), membership.unlimitedClasses(), membership.homeGym()));
  }
}
