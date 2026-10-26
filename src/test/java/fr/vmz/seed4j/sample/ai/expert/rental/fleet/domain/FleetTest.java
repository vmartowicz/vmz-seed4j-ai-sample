package fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain;

import static fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.FleetFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

@UnitTest
class FleetTest {

  private final VehiclesRepository vehicles = mock(VehiclesRepository.class, CALLS_REAL_METHODS);
  private final Fleet fleet = new Fleet(vehicles);

  @Test
  void shouldRegisterVehicle() {
    doReturn(Optional.empty()).when(vehicles).get(new LicencePlate("AB-123-CD"));

    var vehicle = fleet.register(clio("AB-123-CD"));

    verify(vehicles).save(vehicle);
  }

  @Test
  void shouldNotRegisterSamePlateTwice() {
    known(clio());

    assertThatThrownBy(() -> fleet.register(clio("AB-123-CD"))).isExactlyInstanceOf(DuplicatedVehicleException.class);
    verify(vehicles, never()).save(any());
  }

  @Test
  void shouldListRentableVehiclesOfCategoryAtPoint() {
    var rentable = clio();
    var rented = clio("AA-111-AA").register();
    rented.pickUp();
    var elsewhere = clio("BB-222-BB").register();
    elsewhere.transferTo(RentalPoint.VILLEURBANNE);
    var suv = new VehicleToRegister(new LicencePlate("CC-333-CC"), "Peugeot", "3008", Category.SUV, FuelType.DIESEL, 1_000, TODAY, TODAY, RentalPoint.PART_DIEU).register();
    doReturn(List.of(rentable, rented, elsewhere, suv)).when(vehicles).all();

    assertThat(fleet.rentable(RentalPoint.PART_DIEU, Category.ECO, TODAY)).containsExactly(rentable);
  }

  @Test
  void shouldPickUpAndGiveBackVehicle() {
    var vehicle = known(clio());

    fleet.pickUp(vehicle.plate());
    fleet.giveBack(vehicle.plate(), RentalPoint.SAINT_EXUPERY, 20_300, false, TODAY);

    assertThat(vehicle.point()).isEqualTo(RentalPoint.SAINT_EXUPERY);
    verify(vehicles, times(2)).save(vehicle);
  }

  @Test
  void shouldCompleteMaintenanceAndTransfer() {
    var vehicle = known(clio());

    fleet.completeMaintenance(vehicle.plate(), TODAY);
    fleet.transfer(vehicle.plate(), RentalPoint.VILLEURBANNE);

    assertThat(fleet.vehicle(vehicle.plate()).point()).isEqualTo(RentalPoint.VILLEURBANNE);
    verify(vehicles, times(2)).save(vehicle);
  }

  @Test
  void shouldNotFindUnknownVehicle() {
    var unknown = new LicencePlate("ZZ-999-ZZ");
    doReturn(Optional.empty()).when(vehicles).get(unknown);

    assertThatThrownBy(() -> fleet.pickUp(unknown)).isExactlyInstanceOf(UnknownVehicleException.class);
  }

  private Vehicle known(Vehicle vehicle) {
    doReturn(Optional.of(vehicle)).when(vehicles).get(vehicle.plate());

    return vehicle;
  }
}
