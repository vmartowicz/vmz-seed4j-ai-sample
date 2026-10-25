package fr.vmz.seed4j.sample.ai.expert.gym.access.domain;

import java.util.Optional;

public interface BadgesRepository {
  void save(Badge badge);

  Optional<Badge> get(BadgeNumber badge);

  default Badge badge(BadgeNumber badge) {
    return get(badge).orElseThrow(() -> new UnknownBadgeException(badge));
  }
}
