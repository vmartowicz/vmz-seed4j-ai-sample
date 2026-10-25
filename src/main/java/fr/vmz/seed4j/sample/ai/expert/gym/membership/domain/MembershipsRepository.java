package fr.vmz.seed4j.sample.ai.expert.gym.membership.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.util.Optional;

public interface MembershipsRepository {
  void save(Membership membership);

  Optional<Membership> get(MemberId member);

  default Membership membership(MemberId member) {
    return get(member).orElseThrow(() -> new UnknownMembershipException(member));
  }
}
