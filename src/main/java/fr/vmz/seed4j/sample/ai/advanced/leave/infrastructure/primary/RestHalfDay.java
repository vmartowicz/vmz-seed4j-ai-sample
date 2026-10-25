package fr.vmz.seed4j.sample.ai.advanced.leave.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.HalfDay;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

enum RestHalfDay {
  MORNING,
  AFTERNOON;

  static RestHalfDay from(HalfDay half) {
    return valueOf(half.name());
  }

  static @Nullable HalfDay toDomain(@Nullable RestHalfDay half) {
    return Optional.ofNullable(half)
      .map(value -> HalfDay.valueOf(value.name()))
      .orElse(null);
  }
}
