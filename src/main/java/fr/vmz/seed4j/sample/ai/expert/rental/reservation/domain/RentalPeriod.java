package fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.time.Duration;
import java.time.LocalDateTime;

public record RentalPeriod(RentalPoint pickUpPoint, LocalDateTime pickUpAt, RentalPoint returnPoint, LocalDateTime returnAt) {
  private static final long MINUTES_PER_DAY = 24 * 60;

  public RentalPeriod {
    Assert.notNull("pickUpPoint", pickUpPoint);
    Assert.notNull("pickUpAt", pickUpAt);
    Assert.notNull("returnPoint", returnPoint);
    Assert.notNull("returnAt", returnAt);

    if (!returnAt.isAfter(pickUpAt)) {
      throw new InvalidRentalPeriodException(pickUpAt, returnAt);
    }
  }

  public int days() {
    var minutes = Duration.between(pickUpAt, returnAt).toMinutes();

    return (int) ((minutes + MINUTES_PER_DAY - 1) / MINUTES_PER_DAY);
  }
}
