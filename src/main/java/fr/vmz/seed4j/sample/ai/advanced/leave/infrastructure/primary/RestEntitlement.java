package fr.vmz.seed4j.sample.ai.advanced.leave.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.LeaveDays;
import java.math.BigDecimal;
import org.jspecify.annotations.Nullable;

record RestEntitlement(@Nullable BigDecimal days) {
  LeaveDays toDomain() {
    return new LeaveDays(days);
  }
}
