package fr.vmz.seed4j.sample.ai.expert.rental.counter.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.math.BigDecimal;
import java.util.Optional;

public record ReturnInspection(RentalPoint point, int mileage, BigDecimal missingFuel, Optional<String> damage) {
  public ReturnInspection {
    Assert.notNull("point", point);
    Assert.field("missingFuel", missingFuel).min(0);
    Assert.notNull("damage", damage);

    damage = damage.map(String::trim).filter(description -> !description.isEmpty());
  }

  public boolean isDamaged() {
    return damage.isPresent();
  }
}
