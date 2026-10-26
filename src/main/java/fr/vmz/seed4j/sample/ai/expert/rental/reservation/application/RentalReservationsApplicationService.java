package fr.vmz.seed4j.sample.ai.expert.rental.reservation.application;

import fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.LoyaltyCard;
import fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.Reservation;
import fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.ReservationDesk;
import fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.ReservationId;
import fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.ReservationToBook;
import fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.ReservationsRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class RentalReservationsApplicationService {

  private final ReservationsRepository reservations;
  private final ReservationDesk desk;
  private final Clock clock;

  public RentalReservationsApplicationService(ReservationsRepository reservations, LoyaltyCard loyalty, Clock clock) {
    this.reservations = reservations;
    desk = new ReservationDesk(reservations, loyalty);
    this.clock = clock;
  }

  public Reservation book(ReservationToBook booking) {
    return desk.book(booking, LocalDateTime.now(clock));
  }

  public Reservation reservation(ReservationId reservation) {
    return desk.reservation(reservation);
  }

  public Optional<Reservation> find(ReservationId reservation) {
    return reservations.get(reservation);
  }

  public Reservation markPickedUp(ReservationId reservation) {
    return desk.markPickedUp(reservation);
  }

  public Reservation cancel(ReservationId reservation) {
    return desk.cancel(reservation, LocalDateTime.now(clock));
  }
}
