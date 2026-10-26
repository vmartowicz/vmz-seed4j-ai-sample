package fr.vmz.seed4j.sample.ai.expert.rental.fleet.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.rental.fleet.application.FleetApplicationService;
import fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.LicencePlate;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rental/fleet/vehicles")
class VehiclesResource {

  private final FleetApplicationService fleet;

  VehiclesResource(FleetApplicationService fleet) {
    this.fleet = fleet;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  RestVehicle register(@RequestBody RestVehicleToRegister vehicle) {
    return RestVehicle.from(fleet.register(vehicle.toDomain()));
  }

  @GetMapping("/{plate}")
  RestVehicle vehicle(@PathVariable String plate) {
    return RestVehicle.from(fleet.vehicle(new LicencePlate(plate)));
  }

  @GetMapping
  List<RestVehicle> rentable(@RequestParam RentalPoint point, @RequestParam Category category) {
    return fleet.rentable(point, category).stream().map(RestVehicle::from).toList();
  }

  @PostMapping("/{plate}/maintenance-completion")
  RestVehicle completeMaintenance(@PathVariable String plate) {
    return RestVehicle.from(fleet.completeMaintenance(new LicencePlate(plate)));
  }

  @PostMapping("/{plate}/transfer")
  RestVehicle transfer(@PathVariable String plate, @RequestBody RestTransfer transfer) {
    return RestVehicle.from(fleet.transfer(new LicencePlate(plate), transfer.destination()));
  }
}
