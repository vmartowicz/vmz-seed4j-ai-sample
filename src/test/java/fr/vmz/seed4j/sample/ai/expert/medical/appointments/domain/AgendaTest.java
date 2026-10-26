package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

import static fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.AppointmentsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

@UnitTest
class AgendaTest {

  @Test
  void shouldGiveGeneralPractitioners15MinuteSlots() {
    var slots = Agenda.slotsOf(generalPractitioner(), MONDAY);

    assertThat(slots).hasSize(28);
    assertThat(slots.getFirst().start()).isEqualTo(MONDAY.atTime(9, 0));
    assertThat(slots.getFirst().end()).isEqualTo(MONDAY.atTime(9, 15));
    assertThat(slots.get(12).start().toLocalTime()).isEqualTo(LocalTime.of(14, 0));
    assertThat(slots.getLast().start().toLocalTime()).isEqualTo(LocalTime.of(17, 45));
  }

  @Test
  void shouldGiveSpecialists30MinuteSlots() {
    assertThat(Agenda.slotsOf(specialist(), MONDAY)).hasSize(14);
  }

  @Test
  void shouldKeepLastTwoSlotsForEmergencies() {
    var slots = Agenda.slotsOf(generalPractitioner(), MONDAY);

    assertThat(slots.subList(0, 26)).noneMatch(Slot::emergency);
    assertThat(slots.subList(26, 28)).allMatch(Slot::emergency);
  }

  @Test
  void shouldFindSlotStartingAtTime() {
    var doctor = generalPractitioner();

    assertThat(Agenda.slotAt(doctor, MONDAY.atTime(9, 15))).hasValueSatisfying(slot -> assertThat(slot.emergency()).isFalse());
    assertThat(Agenda.slotAt(doctor, MONDAY.atTime(9, 10))).isEmpty();
    assertThat(Agenda.slotAt(doctor, MONDAY.atTime(12, 0))).isEmpty();
  }
}
