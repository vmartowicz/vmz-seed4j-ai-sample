package fr.vmz.seed4j.sample.ai.expert.gym.access.application;

import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.AccessRightsProvider;
import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.Badge;
import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.BadgeNumber;
import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.BadgesRepository;
import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.Entry;
import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.Occupancy;
import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.Presence;
import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.PresencesRepository;
import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.Turnstiles;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class AccessApplicationService {

  private final Turnstiles turnstiles;
  private final Clock clock;

  public AccessApplicationService(BadgesRepository badges, PresencesRepository presences, AccessRightsProvider rights, Clock clock) {
    turnstiles = new Turnstiles(badges, presences, rights);
    this.clock = clock;
  }

  public Badge issueBadge(MemberId member) {
    return turnstiles.issueBadge(member);
  }

  public Entry enter(BadgeNumber badge, Gym gym) {
    return turnstiles.enter(badge, gym, now());
  }

  public Presence exit(BadgeNumber badge) {
    return turnstiles.exit(badge, now());
  }

  public Occupancy occupancy(Gym gym) {
    return turnstiles.occupancy(gym, now());
  }

  private LocalDateTime now() {
    return LocalDateTime.now(clock);
  }
}
