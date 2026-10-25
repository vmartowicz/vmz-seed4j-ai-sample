package fr.vmz.seed4j.sample.ai.advanced.leave.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.DayPart;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeavePeriod;
import java.time.LocalDate;
import org.jspecify.annotations.Nullable;

record RestLeavePeriod(
  @Nullable LocalDate startDate,
  @Nullable RestHalfDay startHalf,
  @Nullable LocalDate endDate,
  @Nullable RestHalfDay endHalf
) {
  LeavePeriod toDomain() {
    return new LeavePeriod(
      new DayPart(startDate, RestHalfDay.toDomain(startHalf)),
      new DayPart(endDate, RestHalfDay.toDomain(endHalf))
    );
  }
}
