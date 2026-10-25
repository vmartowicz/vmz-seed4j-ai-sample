package fr.vmz.seed4j.sample.ai.expert.gym.membership.infrastructure.secondary.membership;

import fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.Membership;
import fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.MembershipsRepository;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryMembershipsRepository implements MembershipsRepository {

  private final Map<MemberId, Membership> memberships = new ConcurrentHashMap<>();

  @Override
  public void save(Membership membership) {
    memberships.put(membership.id(), membership);
  }

  @Override
  public Optional<Membership> get(MemberId member) {
    return Optional.ofNullable(memberships.get(member));
  }
}
