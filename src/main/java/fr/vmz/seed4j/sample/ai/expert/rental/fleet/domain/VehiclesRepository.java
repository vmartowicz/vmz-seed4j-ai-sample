package fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain;

import java.util.List;
import java.util.Optional;

public interface VehiclesRepository {
  void save(Vehicle vehicle);

  Optional<Vehicle> get(LicencePlate plate);

  List<Vehicle> all();

  default Vehicle vehicle(LicencePlate plate) {
    return get(plate).orElseThrow(() -> new UnknownVehicleException(plate));
  }
}
