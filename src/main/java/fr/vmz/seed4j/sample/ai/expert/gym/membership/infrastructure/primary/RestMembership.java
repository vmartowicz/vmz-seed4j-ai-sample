package fr.vmz.seed4j.sample.ai.expert.gym.membership.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.Membership;
import fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.MembershipStatus;
import fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.Plan;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestMembership(
  UUID id,
  String fullName,
  Plan plan,
  Gym homeGym,
  boolean committed,
  BigDecimal monthlyFee,
  LocalDate subscribedOn,
  @Nullable LocalDate commitmentEnd,
  MembershipStatus status
) {
  static RestMembership from(Membership membership, LocalDate today) {
    return new RestMembership(
      membership.id().get(),
      membership.fullName(),
      membership.plan(),
      membership.homeGym(),
      membership.committed(),
      membership.monthlyFee(),
      membership.subscribedOn(),
      membership.commitmentEnd().orElse(null),
      membership.statusOn(today)
    );
  }
}
