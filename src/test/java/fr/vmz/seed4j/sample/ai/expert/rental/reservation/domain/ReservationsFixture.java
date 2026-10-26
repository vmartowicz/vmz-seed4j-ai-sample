package fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain;

import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class ReservationsFixture {

  public static final LocalDateTime NOW = LocalDateTime.of(2026, 6, 1, 10, 0);
  public static final LocalDateTime PICK_UP = LocalDateTime.of(2026, 6, 10, 9, 0);

  private ReservationsFixture() {}

  public static Driver jane() {
    return new Driver("Jane Doe", new Email("jane@mail.com"), LocalDate.of(1990, 5, 4), LocalDate.of(2010, 6, 1));
  }

  public static Driver young() {
    return new Driver("Young Doe", new Email("young@mail.com"), LocalDate.of(2003, 1, 1), LocalDate.of(2021, 1, 1));
  }

  public static RentalPeriod threeDays() {
    return new RentalPeriod(RentalPoint.PART_DIEU, PICK_UP, RentalPoint.PART_DIEU, PICK_UP.plusDays(3));
  }

  public static ReservationToBook eco(Driver driver) {
    return new ReservationToBook(driver, Category.ECO, threeDays(), Options.NONE, false);
  }

  public static Reservation confirmed() {
    return eco(jane()).book(false, NOW);
  }
}
