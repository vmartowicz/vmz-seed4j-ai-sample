package fr.vmz.seed4j.sample.ai.expert.rental.fleet.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.FuelType;
import fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.Vehicle;
import fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.VehicleStatus;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.time.LocalDate;

record RestVehicle(
  String plate,
  String make,
  String model,
  Category category,
  FuelType fuel,
  int mileage,
  RentalPoint point,
  VehicleStatus status,
  LocalDate nextServiceOn,
  int nextServiceMileage
) {
  static RestVehicle from(Vehicle vehicle) {
    return new RestVehicle(
      vehicle.plate().get(),
      vehicle.make(),
      vehicle.model(),
      vehicle.category(),
      vehicle.fuel(),
      vehicle.mileage(),
      vehicle.point(),
      vehicle.status(),
      vehicle.nextServiceOn(),
      vehicle.nextServiceMileage()
    );
  }
}
