package fr.vmz.seed4j.sample.ai.shared.conference.domain;

import java.time.LocalDate;
import java.util.List;

public final class Edition {

  public static final LocalDate FIRST_DAY = LocalDate.of(2026, 10, 15);
  public static final LocalDate SECOND_DAY = LocalDate.of(2026, 10, 16);

  private static final LocalDate CALL_FOR_PAPERS_OPENS = LocalDate.of(2026, 2, 1);
  private static final LocalDate CALL_FOR_PAPERS_CLOSES = LocalDate.of(2026, 3, 31);

  private Edition() {}

  public static List<LocalDate> days() {
    return List.of(FIRST_DAY, SECOND_DAY);
  }

  public static boolean isCallForPapersOpenOn(LocalDate date) {
    return !date.isBefore(CALL_FOR_PAPERS_OPENS) && !date.isAfter(CALL_FOR_PAPERS_CLOSES);
  }
}
