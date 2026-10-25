package fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain;

import java.util.List;
import java.util.Optional;

public interface ConferenceBadgesRepository {
  void save(ConferenceBadge badge);

  Optional<ConferenceBadge> get(BadgeId badge);

  List<ConferenceBadge> ofTicket(String ticket);

  default ConferenceBadge badge(BadgeId badge) {
    return get(badge).orElseThrow(() -> new UnknownConferenceBadgeException(badge));
  }
}
