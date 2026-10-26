package fr.vmz.seed4j.sample.ai.expert.rental.counter.infrastructure.secondary.fleet;

import fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.FleetVehicles;
import fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.RentedVehicle;
import fr.vmz.seed4j.sample.ai.expert.rental.fleet.infrastructure.primary.JavaFleet;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
class CounterFleetVehicles implements FleetVehicles {

  private final JavaFleet fleet;

  CounterFleetVehicles(JavaFleet fleet) {
    this.fleet = fleet;
  }

  @Override
  public List<String> rentable(RentalPoint point, Category category, LocalDate date) {
    return fleet.rentable(point, category, date);
  }

  @Override
  public RentedVehicle pickUp(String plate) {
    var vehicle = fleet.pickUp(plate);

    return new RentedVehicle(vehicle.plate(), vehicle.category(), vehicle.mileage(), vehicle.electric());
  }

  @Override
  public void giveBack(String plate, RentalPoint point, int mileage, boolean damaged, LocalDate date) {
    fleet.giveBack(plate, point, mileage, damaged, date);
  }
}
