package fr.vmz.seed4j.sample.ai.expert.rental.counter.domain;

import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public final class CounterFixture {

  public static final LocalDateTime PICK_UP = LocalDateTime.of(2026, 6, 10, 9, 0);
  public static final Email JANE = new Email("jane@mail.com");

  private CounterFixture() {}

  public static BookedRental booked(Category category, boolean youngDriver) {
    return new BookedRental(
      UUID.randomUUID(),
      JANE,
      youngDriver,
      category,
      RentalPoint.PART_DIEU,
      PICK_UP,
      PICK_UP.plusDays(3),
      3,
      new BigDecimal("35"),
      new BigDecimal("105"),
      true
    );
  }

  public static Contract open() {
    return Contract.open(booked(Category.ECO, false), new RentedVehicle("AB-123-CD", Category.ECO, 20_000, false), PICK_UP);
  }

  public static ReturnInspection inspection(int mileage, String damage) {
    return new ReturnInspection(RentalPoint.SAINT_EXUPERY, mileage, new BigDecimal("5"), Optional.ofNullable(damage));
  }
}
