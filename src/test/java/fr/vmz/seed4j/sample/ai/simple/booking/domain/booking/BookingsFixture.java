package fr.vmz.seed4j.sample.ai.simple.booking.domain.booking;

import static fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.CatalogFixture.*;

import fr.vmz.seed4j.sample.ai.simple.booking.domain.Slot;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class BookingsFixture {

  // Tuesday 2026-03-10 at 10:00, in Paris
  public static final LocalDateTime NOW = LocalDateTime.of(2026, 3, 10, 10, 0);
  public static final LocalDate TOMORROW = NOW.toLocalDate().plusDays(1);
  public static final LocalDateTime TOMORROW_10_AM = TOMORROW.atTime(10, 0);

  private BookingsFixture() {}

  public static Customer customer() {
    return new Customer(new CustomerName("Jane Doe"), new MobilePhone("0612345678"));
  }

  public static Booking massageTomorrowAt10() {
    return massageAt(TOMORROW_10_AM);
  }

  public static Booking massageAt(LocalDateTime start) {
    return new BookingToCreate(MASSAGE.id(), customer(), MASSAGE.slotStartingAt(start)).create();
  }

  public static Slot massageSlotAt(LocalDateTime start) {
    return MASSAGE.slotStartingAt(start);
  }
}
