package fr.vmz.seed4j.sample.ai.expert.gym.membership.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.MembershipToSubscribe;
import fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.Plan;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import java.time.LocalDate;
import org.jspecify.annotations.Nullable;

record RestMembershipToSubscribe(
  @Nullable String fullName,
  @Nullable LocalDate birthDate,
  boolean parentalConsent,
  boolean studentCard,
  @Nullable Plan plan,
  boolean committed,
  @Nullable Gym homeGym
) {
  MembershipToSubscribe toDomain() {
    return new MembershipToSubscribe(fullName, birthDate, parentalConsent, studentCard, plan, committed, homeGym);
  }
}
