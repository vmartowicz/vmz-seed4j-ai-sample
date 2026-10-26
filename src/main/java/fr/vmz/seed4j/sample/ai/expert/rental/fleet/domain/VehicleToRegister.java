package fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.time.LocalDate;

public record VehicleToRegister(
  LicencePlate plate,
  String make,
  String model,
  Category category,
  FuelType fuel,
  int mileage,
  LocalDate firstRegistration,
  LocalDate lastService,
  RentalPoint point
) {
  public VehicleToRegister {
    Assert.notNull("plate", plate);
    Assert.field("make", make).notBlank().maxLength(50);
    Assert.field("model", model).notBlank().maxLength(50);
    Assert.notNull("category", category);
    Assert.notNull("fuel", fuel);
    Assert.field("mileage", mileage).min(0);
    Assert.notNull("firstRegistration", firstRegistration);
    Assert.notNull("lastService", lastService);
    Assert.notNull("point", point);
  }

  public Vehicle register() {
    return new Vehicle(this);
  }
}
