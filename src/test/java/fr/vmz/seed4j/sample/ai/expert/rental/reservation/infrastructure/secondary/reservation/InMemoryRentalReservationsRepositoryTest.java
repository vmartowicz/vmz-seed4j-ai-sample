package fr.vmz.seed4j.sample.ai.expert.rental.reservation.infrastructure.secondary.reservation;

import static fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.ReservationsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.ReservationId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryRentalReservationsRepositoryTest {

  private final InMemoryRentalReservationsRepository reservations = new InMemoryRentalReservationsRepository();

  @Test
  void shouldGetSavedReservation() {
    var reservation = confirmed();

    reservations.save(reservation);

    assertThat(reservations.get(reservation.id())).containsSame(reservation);
    assertThat(reservations.get(ReservationId.newId())).isEmpty();
  }
}
