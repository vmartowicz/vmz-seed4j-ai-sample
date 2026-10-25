package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.LeaveDays;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.Year;
import java.util.List;
import java.util.stream.Stream;

public record LeavePeriod(DayPart start, DayPart end) {
  public LeavePeriod {
    Assert.notNull("start", start);
    Assert.notNull("end", end);

    if (start.compareTo(end) > 0) {
      throw new InvalidLeavePeriodException("This leave ends before it starts");
    }

    if (start.date().getYear() != end.date().getYear()) {
      throw new InvalidLeavePeriodException("A leave can't span two years");
    }

    if (!start.isWorkingDay() || !end.isWorkingDay()) {
      throw new InvalidLeavePeriodException("A leave starts and ends on a working day");
    }
  }

  public List<DayPart> halfDays() {
    return start
      .date()
      .datesUntil(end.date().plusDays(1))
      .flatMap(date -> Stream.of(HalfDay.values()).map(half -> new DayPart(date, half)))
      .filter(DayPart::isWorkingDay)
      .filter(this::contains)
      .toList();
  }

  public LeaveDays days() {
    return LeaveDays.ofHalfDays(halfDays().size());
  }

  public boolean overlaps(LeavePeriod other) {
    return start.compareTo(other.end()) <= 0 && other.start().compareTo(end) <= 0;
  }

  public boolean contains(DayPart part) {
    return start.compareTo(part) <= 0 && part.compareTo(end) <= 0;
  }

  public Year year() {
    return Year.from(start.date());
  }
}
