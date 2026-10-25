package fr.vmz.seed4j.sample.ai.expert.conference.onsite.infrastructure.secondary.badge;

import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.BadgeId;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.ConferenceBadge;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.ConferenceBadgesRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryConferenceBadgesRepository implements ConferenceBadgesRepository {

  private final Map<BadgeId, ConferenceBadge> badges = new ConcurrentHashMap<>();

  @Override
  public void save(ConferenceBadge badge) {
    badges.put(badge.id(), badge);
  }

  @Override
  public Optional<ConferenceBadge> get(BadgeId badge) {
    return Optional.ofNullable(badges.get(badge));
  }

  @Override
  public List<ConferenceBadge> ofTicket(String ticket) {
    return badges
      .values()
      .stream()
      .filter(badge -> badge.ticket().filter(ticket::equals).isPresent())
      .toList();
  }
}
