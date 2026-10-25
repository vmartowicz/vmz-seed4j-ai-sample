package fr.vmz.seed4j.sample.ai.intermediate.parking.domain;

import static fr.vmz.seed4j.sample.ai.intermediate.parking.domain.ReservationsFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class SpacesReserverTest {

  private final ReservationsRepository reservations = mock(ReservationsRepository.class, CALLS_REAL_METHODS);
  private final SpacesReserver reserver = new SpacesReserver(reservations);

  @BeforeEach
  void noReservations() {
    doReturn(List.of()).when(reservations).activeOn(any());
    doReturn(List.of()).when(reservations).activeOf(any());
  }

  @Nested
  class Reserve {

    @Test
    void shouldReserveForToday() {
      var reservation = reserver.reserve(toCreate(1, TODAY), TODAY);

      assertThat(reservation.date()).isEqualTo(TODAY);
      verify(reservations).save(reservation);
    }

    @Test
    void shouldReserve7DaysInAdvance() {
      assertThat(reserver.reserve(toCreate(1, TODAY.plusDays(7)), TODAY).date()).isEqualTo(TODAY.plusDays(7));
    }

    @Test
    void shouldNotReserve8DaysInAdvance() {
      assertThatThrownBy(() -> reserver.reserve(toCreate(1, TODAY.plusDays(8)), TODAY))
        .isExactlyInstanceOf(ReservationOutOfWindowException.class)
        .hasMessageContaining("2026-03-18");
      verify(reservations, never()).save(any());
    }

    @Test
    void shouldNotReserveInThePast() {
      assertThatThrownBy(() -> reserver.reserve(toCreate(1, TODAY.minusDays(1)), TODAY)).isExactlyInstanceOf(
        ReservationOutOfWindowException.class
      );
    }

    @Test
    void shouldNotReserveSpaceTwiceForTheSameDate() {
      doReturn(List.of(reservation(new ResidentName("Marie Curie"), 1, TODAY))).when(reservations).activeOn(TODAY);

      assertThatThrownBy(() -> reserver.reserve(toCreate(1, TODAY), TODAY))
        .isExactlyInstanceOf(SpaceAlreadyReservedException.class)
        .hasMessageContaining("Space 1");
      verify(reservations, never()).save(any());
    }

    @Test
    void shouldReserveAnotherSpaceForTheSameDate() {
      doReturn(List.of(reservation(new ResidentName("Marie Curie"), 1, TODAY))).when(reservations).activeOn(TODAY);

      assertThat(reserver.reserve(toCreate(2, TODAY), TODAY).space()).isEqualTo(new SpaceNumber(2));
    }

    @Test
    void shouldNotReserveMoreThan2SpacesPerMonth() {
      doReturn(List.of(reservation(1, LocalDate.of(2026, 3, 2)), reservation(1, LocalDate.of(2026, 3, 5)))).when(reservations).activeOf(jean());

      assertThatThrownBy(() -> reserver.reserve(toCreate(1, TODAY), TODAY)).isExactlyInstanceOf(MonthlyQuotaReachedException.class);
      verify(reservations, never()).save(any());
    }

    @Test
    void shouldReserveSecondSpaceOfTheMonth() {
      doReturn(List.of(reservation(1, LocalDate.of(2026, 3, 2)))).when(reservations).activeOf(jean());

      assertThat(reserver.reserve(toCreate(1, TODAY), TODAY).date()).isEqualTo(TODAY);
    }

    @Test
    void shouldCountQuotaOnTheMonthOfTheReservedDate() {
      doReturn(List.of(reservation(1, LocalDate.of(2026, 3, 2)), reservation(1, LocalDate.of(2026, 3, 5)))).when(reservations).activeOf(jean());
      var aprilFirst = LocalDate.of(2026, 4, 1);

      assertThat(reserver.reserve(toCreate(1, aprilFirst), LocalDate.of(2026, 3, 28)).date()).isEqualTo(aprilFirst);
    }

    private ReservationToCreate toCreate(int space, LocalDate date) {
      return new ReservationToCreate(jean(), new SpaceNumber(space), date);
    }
  }

  @Test
  void shouldCancelReservation() {
    var reservation = reservation(1, TODAY.plusDays(3));
    doReturn(Optional.of(reservation)).when(reservations).get(reservation.id());

    reserver.cancel(reservation.id(), TODAY.atTime(10, 0));

    assertThat(reservation.status()).isEqualTo(ReservationStatus.CANCELLED);
    verify(reservations).save(reservation);
  }

  @Test
  void shouldNotCancelUnknownReservation() {
    var unknown = ReservationId.newId();
    doReturn(Optional.empty()).when(reservations).get(unknown);

    assertThatThrownBy(() -> reserver.cancel(unknown, TODAY.atTime(10, 0)))
      .isExactlyInstanceOf(UnknownReservationException.class)
      .hasMessageContaining(unknown.get().toString());
  }

  @Test
  void shouldListAvailableSpaces() {
    doReturn(List.of(reservation(2, TODAY), reservation(new ResidentName("Marie Curie"), 7, TODAY))).when(reservations).activeOn(TODAY);

    assertThat(reserver.availableSpaces(TODAY)).extracting(SpaceNumber::get).containsExactly(1, 3, 4, 5, 6, 8, 9, 10);
  }

  @Test
  void shouldListCurrentReservationsSortedByDate() {
    var past = reservation(1, TODAY.minusDays(2));
    var later = reservation(4, TODAY.plusDays(3));
    var todaySpace5 = reservation(5, TODAY);
    var todaySpace2 = reservation(2, TODAY);
    doReturn(List.of(past, later, todaySpace5, todaySpace2)).when(reservations).activeOf(jean());

    assertThat(reserver.currentReservationsOf(jean(), TODAY)).containsExactly(todaySpace2, todaySpace5, later);
  }
}
