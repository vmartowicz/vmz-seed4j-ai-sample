package fr.vmz.seed4j.sample.ai.expert.rental.fleet.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import org.jspecify.annotations.Nullable;

record RestTransfer(@Nullable RentalPoint point) {
  RentalPoint destination() {
    Assert.notNull("point", point);

    return point;
  }
}
