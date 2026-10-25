package fr.vmz.seed4j.sample.ai.intermediate.parking.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;

public class SpacesReserver {

  private static final int BOOKING_WINDOW_IN_DAYS = 7;
  private static final int MONTHLY_QUOTA = 2;
  private static final Comparator<Reservation> CHRONOLOGICALLY = Comparator.comparing(Reservation::date).thenComparing(reservation ->
    reservation.space().get()
  );

  private final ReservationsRepository reservations;

  public SpacesReserver(ReservationsRepository reservations) {
    this.reservations = reservations;
  }

  public Reservation reserve(ReservationToCreate toCreate, LocalDate today) {
    Assert.notNull("toCreate", toCreate);
    Assert.notNull("today", today);

    var date = toCreate.date();
    if (date.isBefore(today) || date.isAfter(today.plusDays(BOOKING_WINDOW_IN_DAYS))) {
      throw new ReservationOutOfWindowException(date);
    }

    if (reservations.activeOn(date).stream().anyMatch(reservation -> reservation.space().equals(toCreate.space()))) {
      throw new SpaceAlreadyReservedException(toCreate);
    }

    var month = YearMonth.from(date);
    var reservationsOfTheMonth = reservations
      .activeOf(toCreate.resident())
      .stream()
      .filter(reservation -> YearMonth.from(reservation.date()).equals(month))
      .count();
    if (reservationsOfTheMonth >= MONTHLY_QUOTA) {
      throw new MonthlyQuotaReachedException(toCreate.resident(), month);
    }

    var reservation = toCreate.create();
    reservations.save(reservation);

    return reservation;
  }

  public Reservation cancel(ReservationId reservationId, LocalDateTime now) {
    var reservation = reservations.reservation(reservationId);
    reservation.cancel(now);
    reservations.save(reservation);

    return reservation;
  }

  public List<SpaceNumber> availableSpaces(LocalDate date) {
    var reserved = reservations.activeOn(date).stream().map(Reservation::space).toList();

    return SpaceNumber.all()
      .stream()
      .filter(space -> !reserved.contains(space))
      .toList();
  }

  public List<Reservation> currentReservationsOf(ResidentName resident, LocalDate today) {
    return reservations
      .activeOf(resident)
      .stream()
      .filter(reservation -> reservation.isCurrentOn(today))
      .sorted(CHRONOLOGICALLY)
      .toList();
  }
}
