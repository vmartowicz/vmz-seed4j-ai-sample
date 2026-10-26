package fr.vmz.seed4j.sample.ai.expert.rental.fleet.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.FuelType;
import fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.Vehicle;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;

public record JavaVehicle(String plate, Category category, int mileage, boolean electric) {
  static JavaVehicle from(Vehicle vehicle) {
    return new JavaVehicle(vehicle.plate().get(), vehicle.category(), vehicle.mileage(), vehicle.fuel() == FuelType.ELECTRIC);
  }
}
