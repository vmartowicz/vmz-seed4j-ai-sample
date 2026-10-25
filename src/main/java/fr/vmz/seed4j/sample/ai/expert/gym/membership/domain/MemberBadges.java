package fr.vmz.seed4j.sample.ai.expert.gym.membership.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;

public interface MemberBadges {
  String issueFor(MemberId member);
}
