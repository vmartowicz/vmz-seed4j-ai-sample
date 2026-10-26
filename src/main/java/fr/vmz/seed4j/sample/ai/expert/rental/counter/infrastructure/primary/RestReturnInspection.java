package fr.vmz.seed4j.sample.ai.expert.rental.counter.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.ReturnInspection;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.math.BigDecimal;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

record RestReturnInspection(@Nullable RentalPoint point, int mileage, @Nullable BigDecimal missingFuel, @Nullable String damage) {
  ReturnInspection toDomain() {
    return new ReturnInspection(point, mileage, missingFuel, Optional.ofNullable(damage));
  }
}
