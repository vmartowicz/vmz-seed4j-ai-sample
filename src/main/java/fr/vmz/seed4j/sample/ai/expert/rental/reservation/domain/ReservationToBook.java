package fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import java.time.LocalDateTime;

public record ReservationToBook(Driver driver, Category category, RentalPeriod period, Options options, boolean freeDay) {
  public ReservationToBook {
    Assert.notNull("driver", driver);
    Assert.notNull("category", category);
    Assert.notNull("period", period);
    Assert.notNull("options", options);
  }

  public Reservation book(boolean gold, LocalDateTime now) {
    var young = driver.isYoungOn(period.pickUpAt().toLocalDate());

    return new Reservation(this, Quote.of(category, period.days(), young, options, gold, freeDay), young, now);
  }
}
