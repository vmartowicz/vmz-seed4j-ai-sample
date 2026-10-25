package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

import static fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table.TablesFixture.*;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.Contact;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.DinerEmail;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.PhoneNumber;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table.PartySize;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class TableBookingsFixture {

  public static final LocalDate TUESDAY = LocalDate.of(2026, 3, 10);
  public static final LocalDateTime NOW = LocalDateTime.of(2026, 3, 7, 10, 0);
  public static final LocalDateTime DINNER_TIME = TUESDAY.atTime(19, 30);

  private TableBookingsFixture() {}

  public static Contact jane() {
    return new Contact("Jane Doe", new DinerEmail("jane@mail.com"), new PhoneNumber("06 12 34 56 78"));
  }

  public static Slot dinner() {
    return new Slot(TUESDAY, LocalTime.of(19, 30));
  }

  public static BookingToCreate bookingFor(int people) {
    return new BookingToCreate(jane(), dinner(), new PartySize(people), "Peanut allergy");
  }

  public static TableBooking confirmedBooking() {
    return bookingFor(2).create(tables(1), false, NOW);
  }

  public static TableBooking pendingBooking() {
    return bookingFor(6).create(tables(17), false, NOW);
  }

  public static TableBooking paidBooking() {
    var booking = pendingBooking();
    booking.payDeposit(new BigDecimal("120"), NOW);

    return booking;
  }
}
