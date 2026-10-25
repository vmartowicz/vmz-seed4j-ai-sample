package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;

public interface MemberNotifications {
  void notify(MemberId member, String message);
}
