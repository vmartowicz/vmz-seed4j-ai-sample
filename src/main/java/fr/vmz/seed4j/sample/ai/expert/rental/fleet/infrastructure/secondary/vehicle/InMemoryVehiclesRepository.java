package fr.vmz.seed4j.sample.ai.expert.rental.fleet.infrastructure.secondary.vehicle;

import fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.LicencePlate;
import fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.Vehicle;
import fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.VehiclesRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryVehiclesRepository implements VehiclesRepository {

  private final Map<LicencePlate, Vehicle> vehicles = new ConcurrentHashMap<>();

  @Override
  public void save(Vehicle vehicle) {
    vehicles.put(vehicle.plate(), vehicle);
  }

  @Override
  public Optional<Vehicle> get(LicencePlate plate) {
    return Optional.ofNullable(vehicles.get(plate));
  }

  @Override
  public List<Vehicle> all() {
    return List.copyOf(vehicles.values());
  }
}
