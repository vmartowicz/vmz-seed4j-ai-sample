package fr.vmz.seed4j.sample.ai.expert.gym.access.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;

public interface AccessRightsProvider {
  AccessRights rightsOf(MemberId member);
}
