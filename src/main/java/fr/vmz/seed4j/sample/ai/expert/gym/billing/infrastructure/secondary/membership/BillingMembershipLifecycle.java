package fr.vmz.seed4j.sample.ai.expert.gym.billing.infrastructure.secondary.membership;

import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.MembershipLifecycle;
import fr.vmz.seed4j.sample.ai.expert.gym.membership.infrastructure.primary.JavaMemberships;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.time.LocalDate;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
class BillingMembershipLifecycle implements MembershipLifecycle {

  private final JavaMemberships memberships;

  // Lazy: memberships open billing accounts, the contexts depend on each other
  BillingMembershipLifecycle(@Lazy JavaMemberships memberships) {
    this.memberships = memberships;
  }

  @Override
  public void activate(MemberId member) {
    memberships.activate(member.get());
  }

  @Override
  public void terminate(MemberId member) {
    memberships.terminate(member.get());
  }

  @Override
  public boolean isSuspendedOn(MemberId member, LocalDate date) {
    return memberships.isSuspendedOn(member.get(), date);
  }
}
