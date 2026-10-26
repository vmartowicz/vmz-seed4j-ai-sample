package fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain;

import static fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain.FleetFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.time.LocalDate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@UnitTest
class VehicleTest {

  @Test
  void shouldNormalizeLicencePlate() {
    assertThat(new LicencePlate(" ab-123-cd ").get()).isEqualTo("AB-123-CD");
  }

  @ParameterizedTest
  @ValueSource(strings = { "AB123CD", "A-123-CD", "AB-12-CD", "AB-123-C4" })
  void shouldNotBuildInvalidLicencePlate(String plate) {
    assertThatThrownBy(() -> new LicencePlate(plate)).isExactlyInstanceOf(InvalidLicencePlateException.class);
  }

  @Test
  void shouldRegisterAvailableVehicle() {
    var vehicle = clio();

    assertThat(vehicle.plate()).isEqualTo(new LicencePlate("AB-123-CD"));
    assertThat(vehicle.make()).isEqualTo("Renault");
    assertThat(vehicle.model()).isEqualTo("Clio");
    assertThat(vehicle.category()).isEqualTo(Category.ECO);
    assertThat(vehicle.fuel()).isEqualTo(FuelType.PETROL);
    assertThat(vehicle.mileage()).isEqualTo(20_000);
    assertThat(vehicle.point()).isEqualTo(RentalPoint.PART_DIEU);
    assertThat(vehicle.status()).isEqualTo(VehicleStatus.AVAILABLE);
    assertThat(vehicle.isRentableAt(RentalPoint.PART_DIEU, TODAY)).isTrue();
  }

  @Test
  void shouldNotRegisterWithoutModel() {
    assertThatThrownBy(() ->
      new VehicleToRegister(new LicencePlate("AB-123-CD"), "Renault", " ", Category.ECO, FuelType.PETROL, 0, TODAY, TODAY, RentalPoint.PART_DIEU)
    ).isInstanceOf(AssertionException.class);
  }

  @Nested
  class Service {

    @Test
    void shouldBeDue12MonthsAfterLastService() {
      var vehicle = clio();

      assertThat(vehicle.isServiceDueOn(TODAY.minusMonths(2).plusMonths(12).minusDays(1))).isFalse();
      assertThat(vehicle.isServiceDueOn(TODAY.minusMonths(2).plusMonths(12))).isTrue();
    }

    @Test
    void shouldBeDue15000KmAfterLastService() {
      var vehicle = rented();

      vehicle.giveBack(RentalPoint.PART_DIEU, 34_999, false, TODAY);
      assertThat(vehicle.isServiceDueOn(TODAY)).isFalse();
      vehicle.pickUp();
      vehicle.giveBack(RentalPoint.PART_DIEU, 35_000, false, TODAY);

      assertThat(vehicle.status()).isEqualTo(VehicleStatus.IN_MAINTENANCE);
    }

    @Test
    void shouldNotRentVehicleDueForService() {
      assertThat(clio().isRentableAt(RentalPoint.PART_DIEU, TODAY.plusYears(1))).isFalse();
    }

    @Test
    void shouldBeAvailableAgainAfterService() {
      var vehicle = rented();
      vehicle.giveBack(RentalPoint.PART_DIEU, 36_000, false, TODAY);

      vehicle.completeMaintenance(TODAY.plusDays(2));

      assertThat(vehicle.status()).isEqualTo(VehicleStatus.AVAILABLE);
      assertThat(vehicle.isServiceDueOn(TODAY.plusDays(2).plusMonths(11))).isFalse();
    }

    @Test
    void shouldServiceAvailableVehicle() {
      var vehicle = clio();

      vehicle.completeMaintenance(TODAY);

      assertThat(vehicle.isServiceDueOn(TODAY.plusMonths(11))).isFalse();
    }

    @Test
    void shouldNotServiceRentedVehicle() {
      assertThatThrownBy(() -> rented().completeMaintenance(TODAY)).isExactlyInstanceOf(InvalidVehicleTransitionException.class);
    }
  }

  @Nested
  class Rental {

    @Test
    void shouldPickUpAvailableVehicle() {
      var vehicle = rented();

      assertThat(vehicle.status()).isEqualTo(VehicleStatus.RENTED);
      assertThat(vehicle.isRentableAt(RentalPoint.PART_DIEU, TODAY)).isFalse();
      assertThatThrownBy(vehicle::pickUp).isExactlyInstanceOf(InvalidVehicleTransitionException.class).hasMessageContaining("RENTED");
    }

    @Test
    void shouldStayWhereItIsReturned() {
      var vehicle = rented();

      vehicle.giveBack(RentalPoint.SAINT_EXUPERY, 20_500, false, TODAY);

      assertThat(vehicle.status()).isEqualTo(VehicleStatus.AVAILABLE);
      assertThat(vehicle.point()).isEqualTo(RentalPoint.SAINT_EXUPERY);
      assertThat(vehicle.mileage()).isEqualTo(20_500);
      assertThat(vehicle.isRentableAt(RentalPoint.SAINT_EXUPERY, TODAY)).isTrue();
      assertThat(vehicle.isRentableAt(RentalPoint.PART_DIEU, TODAY)).isFalse();
    }

    @Test
    void shouldGoToMaintenanceWhenDamaged() {
      var vehicle = rented();

      vehicle.giveBack(RentalPoint.PART_DIEU, 20_500, true, TODAY);

      assertThat(vehicle.status()).isEqualTo(VehicleStatus.IN_MAINTENANCE);
    }

    @Test
    void shouldNotGiveBackWithLowerMileage() {
      var vehicle = rented();

      assertThatThrownBy(() -> vehicle.giveBack(RentalPoint.PART_DIEU, 19_999, false, TODAY)).isExactlyInstanceOf(InvalidMileageException.class);
    }

    @Test
    void shouldNotGiveBackVehicleNotRented() {
      assertThatThrownBy(() -> clio().giveBack(RentalPoint.PART_DIEU, 20_500, false, TODAY)).isExactlyInstanceOf(
        InvalidVehicleTransitionException.class
      );
    }
  }

  @Nested
  class Retirement {

    @Test
    void shouldLeaveFleetAfter150000Km() {
      var vehicle = rented();

      vehicle.giveBack(RentalPoint.PART_DIEU, 150_001, false, TODAY);

      assertThat(vehicle.status()).isEqualTo(VehicleStatus.OUT_OF_SERVICE);
    }

    @Test
    void shouldLeaveFleetAfter5Years() {
      var vehicle = clio();
      var sixthYear = LocalDate.of(2029, 1, 11);

      vehicle.completeMaintenance(sixthYear);

      assertThat(vehicle.status()).isEqualTo(VehicleStatus.OUT_OF_SERVICE);
      assertThatThrownBy(() -> vehicle.completeMaintenance(sixthYear)).isExactlyInstanceOf(InvalidVehicleTransitionException.class);
    }

    @Test
    void shouldNotRentVehicleOlderThan5Years() {
      var old = new VehicleToRegister(new LicencePlate("ZZ-999-ZZ"), "Renault", "Clio", Category.ECO, FuelType.PETROL, 90_000, TODAY.minusYears(5).minusDays(1), TODAY.minusMonths(1), RentalPoint.PART_DIEU)
        .register();

      assertThat(old.isRentableAt(RentalPoint.PART_DIEU, TODAY)).isFalse();
    }

    @Test
    void shouldStayInFleetUntil5Years() {
      var vehicle = clio();

      vehicle.completeMaintenance(LocalDate.of(2029, 1, 10));

      assertThat(vehicle.status()).isEqualTo(VehicleStatus.AVAILABLE);
    }
  }

  @Test
  void shouldTransferAvailableVehicle() {
    var vehicle = clio();

    vehicle.transferTo(RentalPoint.VILLEURBANNE);

    assertThat(vehicle.point()).isEqualTo(RentalPoint.VILLEURBANNE);
    assertThatThrownBy(() -> rented().transferTo(RentalPoint.VILLEURBANNE)).isExactlyInstanceOf(InvalidVehicleTransitionException.class);
  }
}
