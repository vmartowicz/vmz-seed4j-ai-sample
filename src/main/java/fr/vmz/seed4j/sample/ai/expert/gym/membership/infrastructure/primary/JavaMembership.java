package fr.vmz.seed4j.sample.ai.expert.gym.membership.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.Membership;
import fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.MembershipStatus;
import fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.Plan;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import java.time.LocalDate;
import java.util.Set;

public record JavaMembership(boolean active, boolean suspended, boolean unlimitedClasses, Gym homeGym, Set<Gym> gyms) {
  static JavaMembership from(Membership membership, LocalDate today) {
    var status = membership.statusOn(today);

    return new JavaMembership(
      status == MembershipStatus.ACTIVE,
      status == MembershipStatus.SUSPENDED,
      membership.plan() == Plan.PREMIUM,
      membership.homeGym(),
      membership.accessibleGyms()
    );
  }
}
