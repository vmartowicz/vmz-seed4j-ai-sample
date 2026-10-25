package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;

public record DayPart(LocalDate date, HalfDay half) implements Comparable<DayPart> {
  private static final Comparator<DayPart> CHRONOLOGICALLY = Comparator.comparing(DayPart::date).thenComparing(DayPart::half);

  public DayPart {
    Assert.notNull("date", date);
    Assert.notNull("half", half);
  }

  public boolean isWorkingDay() {
    return date.getDayOfWeek().compareTo(DayOfWeek.FRIDAY) <= 0;
  }

  @Override
  public int compareTo(DayPart other) {
    return CHRONOLOGICALLY.compare(this, other);
  }
}
