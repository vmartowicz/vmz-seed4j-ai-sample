package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.util.Optional;

public interface MemberPlans {
  Optional<MemberPlan> planOf(MemberId member);
}
