package fr.vmz.seed4j.sample.ai.expert.gym.booking.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.SessionId;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestClassBookingRequest(@Nullable UUID memberId, @Nullable UUID sessionId) {
  MemberId member() {
    return new MemberId(memberId);
  }

  SessionId session() {
    return new SessionId(sessionId);
  }
}
