package fr.vmz.seed4j.sample.ai.expert.rental.counter.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestPickUp(@Nullable UUID reservationId) {
  UUID reservation() {
    Assert.notNull("reservationId", reservationId);

    return reservationId;
  }
}
