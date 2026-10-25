package fr.vmz.seed4j.sample.ai.expert.gym.schedule.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.ClassSession;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import java.time.LocalDateTime;
import java.util.UUID;

public record JavaClassSession(UUID id, Gym gym, LocalDateTime start, int capacity, boolean cancelled) {
  static JavaClassSession from(ClassSession session) {
    return new JavaClassSession(session.id().get(), session.studio().gym(), session.start(), session.capacity(), session.isCancelled());
  }
}
