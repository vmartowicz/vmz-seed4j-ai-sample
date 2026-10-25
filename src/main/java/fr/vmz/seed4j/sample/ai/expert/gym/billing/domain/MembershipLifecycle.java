package fr.vmz.seed4j.sample.ai.expert.gym.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.time.LocalDate;

public interface MembershipLifecycle {
  void activate(MemberId member);

  void terminate(MemberId member);

  boolean isSuspendedOn(MemberId member, LocalDate date);
}
