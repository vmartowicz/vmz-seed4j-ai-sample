package fr.vmz.seed4j.sample.ai.expert.rental.counter.domain;

import static fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.CounterFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@UnitTest
class CounterDeskTest {

  private final ContractsRepository contracts = mock(ContractsRepository.class, CALLS_REAL_METHODS);
  private final Reservations reservations = mock(Reservations.class);
  private final FleetVehicles fleet = mock(FleetVehicles.class);
  private final CustomerStatus customers = mock(CustomerStatus.class);
  private final Cashier cashier = mock(Cashier.class);
  private final CounterDesk desk = new CounterDesk(contracts, reservations, fleet, customers, cashier);

  @BeforeEach
  void loadFleet() {
    doReturn(List.of()).when(fleet).rentable(any(), any(), any());
  }

  @Test
  void shouldPickUpVehicleOfBookedCategory() {
    var booked = known(booked(Category.ECO, false));
    available(Category.ECO, "AB-123-CD");

    var contract = desk.pickUp(booked.reservation(), PICK_UP);

    assertThat(contract.plate()).isEqualTo("AB-123-CD");
    verify(reservations).markPickedUp(booked.reservation());
    verify(contracts).save(contract);
    verify(cashier).holdDeposit(contract.id(), JANE, new BigDecimal("800"));
  }

  @Test
  void shouldUpgradeWhenBookedCategoryIsMissing() {
    var booked = known(booked(Category.ECO, false));
    available(Category.SUV, "SU-123-VV");

    assertThat(desk.pickUp(booked.reservation(), PICK_UP).vehicleCategory()).isEqualTo(Category.SUV);
  }

  @Test
  void shouldUpgradeGoldMembers() {
    var booked = known(booked(Category.ECO, false));
    doReturn(true).when(customers).isGold(JANE);
    available(Category.ECO, "AB-123-CD");
    available(Category.COMFORT, "CO-123-MF");

    assertThat(desk.pickUp(booked.reservation(), PICK_UP).vehicleCategory()).isEqualTo(Category.COMFORT);
  }

  @Test
  void shouldNotPickUpWithoutVehicle() {
    var booked = known(booked(Category.SUV, true));
    available(Category.PREMIUM, "PR-123-EM");

    assertThatThrownBy(() -> desk.pickUp(booked.reservation(), PICK_UP)).isExactlyInstanceOf(NoVehicleAvailableException.class);
    verify(fleet, never()).pickUp(any());
  }

  @Test
  void shouldNotPickUpUnconfirmedReservation() {
    var cancelled = booked(Category.ECO, false);
    var unconfirmed = new BookedRental(
      cancelled.reservation(),
      JANE,
      false,
      Category.ECO,
      RentalPoint.PART_DIEU,
      PICK_UP,
      PICK_UP.plusDays(3),
      3,
      new BigDecimal("35"),
      new BigDecimal("105"),
      false
    );
    known(unconfirmed);

    assertThatThrownBy(() -> desk.pickUp(unconfirmed.reservation(), PICK_UP)).isExactlyInstanceOf(ReservationNotConfirmedException.class);
  }

  @Test
  void shouldNotPickUpUnknownReservation() {
    var unknown = UUID.randomUUID();
    doReturn(Optional.empty()).when(reservations).reservation(unknown);

    assertThatThrownBy(() -> desk.pickUp(unknown, PICK_UP)).isExactlyInstanceOf(UnknownBookedRentalException.class);
  }

  @Test
  void shouldGiveBackVehicleAndInvoiceReturn() {
    var contract = open();
    doReturn(Optional.of(contract)).when(contracts).get(contract.id());
    var invoice = UUID.randomUUID();
    doReturn(invoice).when(cashier).finalInvoice(any());

    desk.giveBack(contract.id(), inspection(20_400, "Scratched door"), PICK_UP.plusDays(3));

    assertThat(contract.invoice()).contains(invoice);
    verify(fleet).giveBack("AB-123-CD", RentalPoint.SAINT_EXUPERY, 20_400, true, PICK_UP.plusDays(3).toLocalDate());
    verify(cashier).finalInvoice(argThat(rentalReturn -> rentalReturn.kilometres() == 400));
    verify(contracts).save(contract);
  }

  @Test
  void shouldNotGiveBackUnknownContract() {
    var unknown = ContractId.newId();
    doReturn(Optional.empty()).when(contracts).get(unknown);

    assertThatThrownBy(() -> desk.contract(unknown)).isExactlyInstanceOf(UnknownContractException.class);
  }

  private BookedRental known(BookedRental booked) {
    doReturn(Optional.of(booked)).when(reservations).reservation(booked.reservation());

    return booked;
  }

  private void available(Category category, String plate) {
    doReturn(List.of(plate)).when(fleet).rentable(eq(RentalPoint.PART_DIEU), eq(category), any());
    doReturn(new RentedVehicle(plate, category, 20_000, false)).when(fleet).pickUp(plate);
  }
}
