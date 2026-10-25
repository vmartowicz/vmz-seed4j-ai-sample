package fr.vmz.seed4j.sample.ai.intermediate.parking.domain;

import static fr.vmz.seed4j.sample.ai.intermediate.parking.domain.ReservationsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

@UnitTest
class ReservationTest {

  private static final LocalDate MARCH_12 = LocalDate.of(2026, 3, 12);

  @Test
  void shouldCreateActiveReservation() {
    var reservation = reservation(3, MARCH_12);

    assertThat(reservation.id()).isNotNull();
    assertThat(reservation.resident()).isEqualTo(jean());
    assertThat(reservation.space()).isEqualTo(new SpaceNumber(3));
    assertThat(reservation.date()).isEqualTo(MARCH_12);
    assertThat(reservation.status()).isEqualTo(ReservationStatus.ACTIVE);
  }

  @Test
  void shouldCancelExactly24HoursBefore() {
    var reservation = reservation(3, MARCH_12);

    reservation.cancel(MARCH_12.minusDays(1).atStartOfDay());

    assertThat(reservation.status()).isEqualTo(ReservationStatus.CANCELLED);
  }

  @Test
  void shouldNotCancelLessThan24HoursBefore() {
    var reservation = reservation(3, MARCH_12);

    assertThatThrownBy(() -> reservation.cancel(MARCH_12.minusDays(1).atTime(0, 1)))
      .isExactlyInstanceOf(CancellationDeadlinePassedException.class)
      .hasMessageContaining(reservation.id().get().toString());
    assertThat(reservation.status()).isEqualTo(ReservationStatus.ACTIVE);
  }

  @Test
  void shouldNotCancelCancelledReservation() {
    var reservation = reservation(3, MARCH_12);
    reservation.cancel(TODAY.atStartOfDay());

    assertThatThrownBy(() -> reservation.cancel(TODAY.atStartOfDay())).isExactlyInstanceOf(ReservationAlreadyCancelledException.class);
  }

  @Test
  void shouldBeCurrentFromToday() {
    assertThat(reservation(3, TODAY).isCurrentOn(TODAY)).isTrue();
  }

  @Test
  void shouldNotBeCurrentOncePast() {
    assertThat(reservation(3, TODAY.minusDays(1)).isCurrentOn(TODAY)).isFalse();
  }

  @Test
  void shouldNotBeCurrentOnceCancelled() {
    var reservation = reservation(3, MARCH_12);
    reservation.cancel(TODAY.atStartOfDay());

    assertThat(reservation.isCurrentOn(TODAY)).isFalse();
  }
}
