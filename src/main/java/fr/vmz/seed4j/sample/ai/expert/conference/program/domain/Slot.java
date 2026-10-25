package fr.vmz.seed4j.sample.ai.expert.conference.program.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Edition;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public record Slot(LocalDate day, int number) {
  private static final List<LocalTime> STARTS = List.of(
    LocalTime.of(9, 0),
    LocalTime.of(9, 50),
    LocalTime.of(10, 40),
    LocalTime.of(11, 30),
    LocalTime.of(14, 0),
    LocalTime.of(14, 50),
    LocalTime.of(15, 40),
    LocalTime.of(16, 30),
    LocalTime.of(17, 20)
  );
  private static final Set<Integer> LAST_OF_HALF_DAY = Set.of(4, 9);
  private static final int MINUTES = 40;

  public Slot {
    Assert.notNull("day", day);

    if (!Edition.days().contains(day) || number < 1 || number > STARTS.size()) {
      throw new InvalidSlotException(day, number);
    }
  }

  public LocalDateTime start() {
    return day.atTime(STARTS.get(number - 1));
  }

  public LocalDateTime end() {
    return start().plusMinutes(MINUTES);
  }

  public Optional<Slot> next() {
    if (LAST_OF_HALF_DAY.contains(number)) {
      return Optional.empty();
    }

    return Optional.of(new Slot(day, number + 1));
  }

  public boolean isKeynote() {
    return day.equals(Edition.FIRST_DAY) && number == 1;
  }
}
