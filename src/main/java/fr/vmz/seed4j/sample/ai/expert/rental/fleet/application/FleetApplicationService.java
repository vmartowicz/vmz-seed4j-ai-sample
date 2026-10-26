package fr.vmz.seed4j.sample.ai.expert.rental.fleet.application;

import fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.Fleet;
import fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.LicencePlate;
import fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.Vehicle;
import fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.VehicleToRegister;
import fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.VehiclesRepository;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FleetApplicationService {

  private final Fleet fleet;
  private final Clock clock;

  public FleetApplicationService(VehiclesRepository vehicles, Clock clock) {
    fleet = new Fleet(vehicles);
    this.clock = clock;
  }

  public Vehicle register(VehicleToRegister vehicle) {
    return fleet.register(vehicle);
  }

  public Vehicle vehicle(LicencePlate plate) {
    return fleet.vehicle(plate);
  }

  public List<Vehicle> rentable(RentalPoint point, Category category) {
    return rentable(point, category, today());
  }

  public List<Vehicle> rentable(RentalPoint point, Category category, LocalDate date) {
    return fleet.rentable(point, category, date);
  }

  public Vehicle pickUp(LicencePlate plate) {
    return fleet.pickUp(plate);
  }

  public Vehicle giveBack(LicencePlate plate, RentalPoint point, int mileage, boolean damaged, LocalDate date) {
    return fleet.giveBack(plate, point, mileage, damaged, date);
  }

  public Vehicle completeMaintenance(LicencePlate plate) {
    return fleet.completeMaintenance(plate, today());
  }

  public Vehicle transfer(LicencePlate plate, RentalPoint destination) {
    return fleet.transfer(plate, destination);
  }

  private LocalDate today() {
    return LocalDate.now(clock);
  }
}
