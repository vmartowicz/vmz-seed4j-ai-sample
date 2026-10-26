package fr.vmz.seed4j.sample.ai.expert.rental.fleet.infrastructure.secondary.vehicle;

import static fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.FleetFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.LicencePlate;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryVehiclesRepositoryTest {

  private final InMemoryVehiclesRepository vehicles = new InMemoryVehiclesRepository();

  @Test
  void shouldGetSavedVehicles() {
    var vehicle = clio();

    vehicles.save(vehicle);

    assertThat(vehicles.get(vehicle.plate())).containsSame(vehicle);
    assertThat(vehicles.get(new LicencePlate("ZZ-999-ZZ"))).isEmpty();
    assertThat(vehicles.all()).containsExactly(vehicle);
  }
}
