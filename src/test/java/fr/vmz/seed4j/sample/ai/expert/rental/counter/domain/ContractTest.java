package fr.vmz.seed4j.sample.ai.expert.rental.counter.domain;

import static fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.CounterFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@UnitTest
class ContractTest {

  @Test
  void shouldOpenContractWithDepositOfBookedCategory() {
    var booked = booked(Category.ECO, false);

    var contract = Contract.open(booked, new RentedVehicle("AB-123-CD", Category.SUV, 20_000, true), PICK_UP.plusMinutes(5));

    assertThat(contract.id()).isNotNull();
    assertThat(contract.reservation()).isEqualTo(booked.reservation());
    assertThat(contract.customer()).isEqualTo(JANE);
    assertThat(contract.bookedCategory()).isEqualTo(Category.ECO);
    assertThat(contract.plate()).isEqualTo("AB-123-CD");
    assertThat(contract.vehicleCategory()).isEqualTo(Category.SUV);
    assertThat(contract.pickedUpAt()).isEqualTo(PICK_UP.plusMinutes(5));
    assertThat(contract.plannedReturnAt()).isEqualTo(PICK_UP.plusDays(3));
    assertThat(contract.mileageOut()).isEqualTo(20_000);
    assertThat(contract.deposit()).isEqualByComparingTo("800");
    assertThat(contract.status()).isEqualTo(ContractStatus.OPEN);
    assertThat(contract.returnInspection()).isEmpty();
    assertThat(contract.invoice()).isEmpty();
  }

  @Test
  void shouldCloseContractWithReturnInspection() {
    var contract = open();
    var returnedAt = PICK_UP.plusDays(3).plusMinutes(70);

    var rentalReturn = contract.close(inspection(20_641, "Scratched door"), returnedAt);

    assertThat(contract.status()).isEqualTo(ContractStatus.CLOSED);
    assertThat(contract.returnedAt()).contains(returnedAt);
    assertThat(contract.returnInspection()).contains(inspection(20_641, "Scratched door"));
    assertThat(rentalReturn.kilometres()).isEqualTo(641);
    assertThat(rentalReturn.returnPoint()).isEqualTo(RentalPoint.SAINT_EXUPERY);
    assertThat(rentalReturn.missingFuel()).isEqualByComparingTo("5");
    assertThat(rentalReturn.damaged()).isTrue();
    assertThat(rentalReturn.electric()).isFalse();
    assertThat(rentalReturn.paid()).isEqualByComparingTo("105");
  }

  @Test
  void shouldNotCloseContractTwice() {
    var contract = open();
    contract.close(inspection(20_100, null), PICK_UP.plusDays(3));

    assertThatThrownBy(() -> contract.close(inspection(20_100, null), PICK_UP.plusDays(3))).isExactlyInstanceOf(ContractAlreadyClosedException.class);
  }

  @Test
  void shouldNotReturnWithLowerMileage() {
    assertThatThrownBy(() -> open().close(inspection(19_999, null), PICK_UP.plusDays(3))).isExactlyInstanceOf(InvalidReturnMileageException.class);
  }

  @Test
  void shouldKeepFinalInvoice() {
    var contract = open();
    var invoice = UUID.randomUUID();

    contract.attachInvoice(invoice);

    assertThat(contract.invoice()).contains(invoice);
  }

  @Test
  void shouldIgnoreBlankDamage() {
    assertThat(inspection(20_100, " ").damage()).isEmpty();
    assertThat(inspection(20_100, null).isDamaged()).isFalse();
  }

  @Test
  void shouldNotInspectNegativeFuel() {
    assertThatThrownBy(() -> new ReturnInspection(RentalPoint.PART_DIEU, 1, new BigDecimal("-1"), Optional.empty())).isInstanceOf(AssertionException.class);
  }
}
