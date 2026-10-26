package fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain;

import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.time.LocalDate;

public final class FleetFixture {

  public static final LocalDate TODAY = LocalDate.of(2026, 6, 1);

  private FleetFixture() {}

  public static VehicleToRegister clio(String plate) {
    return new VehicleToRegister(new LicencePlate(plate), "Renault", "Clio", Category.ECO, FuelType.PETROL, 20_000, LocalDate.of(2024, 1, 10), TODAY.minusMonths(2), RentalPoint.PART_DIEU);
  }

  public static Vehicle clio() {
    return clio("AB-123-CD").register();
  }

  public static Vehicle rented() {
    var vehicle = clio();
    vehicle.pickUp();

    return vehicle;
  }
}
