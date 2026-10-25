package fr.vmz.seed4j.sample.ai.expert.gym.access.infrastructure.secondary.badge;

import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.Badge;
import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.BadgeNumber;
import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.BadgesRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryBadgesRepository implements BadgesRepository {

  private final Map<BadgeNumber, Badge> badges = new ConcurrentHashMap<>();

  @Override
  public void save(Badge badge) {
    badges.put(badge.number(), badge);
  }

  @Override
  public Optional<Badge> get(BadgeNumber badge) {
    return Optional.ofNullable(badges.get(badge));
  }
}
