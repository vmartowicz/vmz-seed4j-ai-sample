package fr.vmz.seed4j.sample.ai.expert.gym.access.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.Presence;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import java.time.LocalDateTime;
import org.jspecify.annotations.Nullable;

record RestPresence(Gym gym, LocalDateTime enteredAt, @Nullable LocalDateTime exitedAt) {
  static RestPresence from(Presence presence) {
    return new RestPresence(presence.gym(), presence.enteredAt(), presence.exitedAt().orElse(null));
  }
}
