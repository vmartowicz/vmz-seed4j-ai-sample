package fr.vmz.seed4j.sample.ai.expert.rental.fleet.infrastructure.primary;

import static fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.FleetFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.FuelType;
import fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.LicencePlate;
import fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.VehicleToRegister;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import org.junit.jupiter.api.Test;

@UnitTest
class JavaVehicleTest {

  @Test
  void shouldTellElectricVehicles() {
    var zoe = new VehicleToRegister(new LicencePlate("EV-123-EV"), "Renault", "Zoe", Category.ECO, FuelType.ELECTRIC, 10, TODAY, TODAY, RentalPoint.PART_DIEU)
      .register();

    assertThat(JavaVehicle.from(zoe)).isEqualTo(new JavaVehicle("EV-123-EV", Category.ECO, 10, true));
    assertThat(JavaVehicle.from(clio()).electric()).isFalse();
  }
}
