package fr.vmz.seed4j.sample.ai.intermediate.parking.infrastructure.secondary.reservation;

import static fr.vmz.seed4j.sample.ai.intermediate.parking.domain.ReservationsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.intermediate.parking.domain.ReservationId;
import fr.vmz.seed4j.sample.ai.intermediate.parking.domain.ResidentName;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryReservationsRepositoryTest {

  private final InMemoryReservationsRepository reservations = new InMemoryReservationsRepository();

  @Test
  void shouldGetSavedReservation() {
    var reservation = reservation(1, TODAY);
    reservations.save(reservation);

    assertThat(reservations.get(reservation.id())).contains(reservation);
    assertThat(reservations.get(ReservationId.newId())).isEmpty();
  }

  @Test
  void shouldGetActiveReservationsOfTheDate() {
    var active = reservation(1, TODAY);
    var otherDate = reservation(2, TODAY.plusDays(1));
    var cancelled = reservation(3, TODAY.plusDays(5));
    cancelled.cancel(TODAY.atStartOfDay());
    reservations.save(active);
    reservations.save(otherDate);
    reservations.save(cancelled);

    assertThat(reservations.activeOn(TODAY)).containsExactly(active);
    assertThat(reservations.activeOn(TODAY.plusDays(5))).isEmpty();
  }

  @Test
  void shouldGetActiveReservationsOfResidentIgnoringCase() {
    var jeans = reservation(1, TODAY);
    var cancelled = reservation(2, TODAY.plusDays(4));
    cancelled.cancel(TODAY.atStartOfDay());
    reservations.save(jeans);
    reservations.save(cancelled);
    reservations.save(reservation(new ResidentName("Marie Curie"), 3, TODAY));

    assertThat(reservations.activeOf(new ResidentName("JEAN dupont"))).containsExactly(jeans);
  }
}
