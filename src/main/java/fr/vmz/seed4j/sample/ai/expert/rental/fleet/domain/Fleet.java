package fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.time.LocalDate;
import java.util.List;

public class Fleet {

  private final VehiclesRepository vehicles;

  public Fleet(VehiclesRepository vehicles) {
    this.vehicles = vehicles;
  }

  public Vehicle register(VehicleToRegister toRegister) {
    Assert.notNull("vehicle", toRegister);

    if (vehicles.get(toRegister.plate()).isPresent()) {
      throw new DuplicatedVehicleException(toRegister.plate());
    }

    var vehicle = toRegister.register();
    vehicles.save(vehicle);

    return vehicle;
  }

  public List<Vehicle> rentable(RentalPoint point, Category category, LocalDate date) {
    return vehicles
      .all()
      .stream()
      .filter(vehicle -> vehicle.category() == category && vehicle.isRentableAt(point, date))
      .toList();
  }

  public Vehicle pickUp(LicencePlate plate) {
    var vehicle = vehicles.vehicle(plate);

    vehicle.pickUp();
    vehicles.save(vehicle);

    return vehicle;
  }

  public Vehicle giveBack(LicencePlate plate, RentalPoint point, int mileage, boolean damaged, LocalDate today) {
    var vehicle = vehicles.vehicle(plate);

    vehicle.giveBack(point, mileage, damaged, today);
    vehicles.save(vehicle);

    return vehicle;
  }

  public Vehicle completeMaintenance(LicencePlate plate, LocalDate today) {
    var vehicle = vehicles.vehicle(plate);

    vehicle.completeMaintenance(today);
    vehicles.save(vehicle);

    return vehicle;
  }

  public Vehicle transfer(LicencePlate plate, RentalPoint destination) {
    var vehicle = vehicles.vehicle(plate);

    vehicle.transferTo(destination);
    vehicles.save(vehicle);

    return vehicle;
  }

  public Vehicle vehicle(LicencePlate plate) {
    return vehicles.vehicle(plate);
  }
}
