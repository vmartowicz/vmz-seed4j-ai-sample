package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public record HourSlot(LocalDateTime start) {
  private static final int FIRST_HOUR = 8;
  private static final int LAST_HOUR = 18;

  public HourSlot {
    Assert.notNull("start", start);

    if (
      start.getDayOfWeek().compareTo(DayOfWeek.FRIDAY) > 0 ||
      start.getHour() < FIRST_HOUR ||
      start.getHour() > LAST_HOUR ||
      !start.truncatedTo(ChronoUnit.HOURS).equals(start)
    ) {
      throw new InvalidHourSlotException(start);
    }
  }

  public LocalDateTime end() {
    return start.plusHours(1);
  }
}
