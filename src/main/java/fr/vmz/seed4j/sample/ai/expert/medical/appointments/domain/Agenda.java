package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class Agenda {

  private static final List<LocalTime[]> OPENING_HOURS = List.of(
    new LocalTime[] { LocalTime.of(9, 0), LocalTime.of(12, 0) },
    new LocalTime[] { LocalTime.of(14, 0), LocalTime.of(18, 0) }
  );
  private static final int EMERGENCY_SLOTS = 2;

  private Agenda() {}

  public static List<Slot> slotsOf(Doctor doctor, LocalDate date) {
    var length = doctor.kind().slotMinutes();
    var starts = new ArrayList<LocalDateTime>();
    for (var hours : OPENING_HOURS) {
      for (var time = hours[0]; time.isBefore(hours[1]); time = time.plusMinutes(length)) {
        starts.add(date.atTime(time));
      }
    }

    var slots = new ArrayList<Slot>();
    for (int index = 0; index < starts.size(); index++) {
      var start = starts.get(index);
      slots.add(new Slot(start, start.plusMinutes(length), index >= starts.size() - EMERGENCY_SLOTS));
    }

    return List.copyOf(slots);
  }

  public static Optional<Slot> slotAt(Doctor doctor, LocalDateTime start) {
    return slotsOf(doctor, start.toLocalDate())
      .stream()
      .filter(slot -> slot.start().equals(start))
      .findFirst();
  }
}
