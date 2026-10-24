package fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog;

import fr.vmz.seed4j.sample.ai.simple.booking.domain.Slot;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record BookableService(ServiceId id, ServiceName name, Duration duration) {
  private static final LocalTime OPENING = LocalTime.of(9, 0);
  private static final Duration OPENING_HOURS = Duration.between(OPENING, LocalTime.of(18, 0));

  public BookableService {
    Assert.notNull("id", id);
    Assert.notNull("name", name);
    Assert.notNull("duration", duration);
  }

  public Slot slotStartingAt(LocalDateTime start) {
    Assert.notNull("start", start);

    var sinceOpening = Duration.between(OPENING, start.toLocalTime());
    if (
      start.getDayOfWeek() == DayOfWeek.SUNDAY ||
      sinceOpening.isNegative() ||
      sinceOpening.plus(duration).compareTo(OPENING_HOURS) > 0 ||
      sinceOpening.toNanos() % duration.toNanos() != 0
    ) {
      throw new InvalidSlotException(start);
    }

    return new Slot(start, start.plus(duration));
  }
}
