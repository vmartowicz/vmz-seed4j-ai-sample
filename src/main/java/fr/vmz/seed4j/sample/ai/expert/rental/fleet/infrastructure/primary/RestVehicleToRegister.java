package fr.vmz.seed4j.sample.ai.expert.rental.fleet.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.FuelType;
import fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.LicencePlate;
import fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.VehicleToRegister;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.time.LocalDate;
import org.jspecify.annotations.Nullable;

record RestVehicleToRegister(
  @Nullable String plate,
  @Nullable String make,
  @Nullable String model,
  @Nullable Category category,
  @Nullable FuelType fuel,
  int mileage,
  @Nullable LocalDate firstRegistration,
  @Nullable LocalDate lastService,
  @Nullable RentalPoint point
) {
  VehicleToRegister toDomain() {
    return new VehicleToRegister(new LicencePlate(plate), make, model, category, fuel, mileage, firstRegistration, lastService, point);
  }
}
