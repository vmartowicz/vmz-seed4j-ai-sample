package fr.vmz.seed4j.sample.ai.expert.rental.fleet.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.rental.fleet.application.FleetApplicationService;
import fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.LicencePlate;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class JavaFleet {

  private final FleetApplicationService fleet;

  public JavaFleet(FleetApplicationService fleet) {
    this.fleet = fleet;
  }

  public List<String> rentable(RentalPoint point, Category category, LocalDate date) {
    return fleet
      .rentable(point, category, date)
      .stream()
      .map(vehicle -> vehicle.plate().get())
      .toList();
  }

  public JavaVehicle pickUp(String plate) {
    return JavaVehicle.from(fleet.pickUp(new LicencePlate(plate)));
  }

  public void giveBack(String plate, RentalPoint point, int mileage, boolean damaged, LocalDate date) {
    fleet.giveBack(new LicencePlate(plate), point, mileage, damaged, date);
  }
}
