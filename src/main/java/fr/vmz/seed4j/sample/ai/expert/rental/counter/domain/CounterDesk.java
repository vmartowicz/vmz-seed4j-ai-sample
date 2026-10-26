package fr.vmz.seed4j.sample.ai.expert.rental.counter.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class CounterDesk {

  private final ContractsRepository contracts;
  private final Reservations reservations;
  private final FleetVehicles fleet;
  private final CustomerStatus customers;
  private final Cashier cashier;

  public CounterDesk(ContractsRepository contracts, Reservations reservations, FleetVehicles fleet, CustomerStatus customers, Cashier cashier) {
    this.contracts = contracts;
    this.reservations = reservations;
    this.fleet = fleet;
    this.customers = customers;
    this.cashier = cashier;
  }

  public Contract pickUp(UUID reservation, LocalDateTime now) {
    var booked = reservations.reservation(reservation).orElseThrow(() -> new UnknownBookedRentalException(reservation));
    if (!booked.confirmed()) {
      throw new ReservationNotConfirmedException(reservation);
    }

    var plate = VehicleChoice.categoriesFor(booked.category(), customers.isGold(booked.customer()), booked.youngDriver())
      .stream()
      .flatMap(category -> fleet.rentable(booked.pickUpPoint(), category, now.toLocalDate()).stream())
      .findFirst()
      .orElseThrow(() -> new NoVehicleAvailableException(booked.category()));

    var contract = Contract.open(booked, fleet.pickUp(plate), now);
    reservations.markPickedUp(reservation);
    contracts.save(contract);
    cashier.holdDeposit(contract.id(), contract.customer(), contract.deposit());

    return contract;
  }

  public Contract giveBack(ContractId id, ReturnInspection inspection, LocalDateTime now) {
    var contract = contracts.contract(id);

    var rentalReturn = contract.close(inspection, now);
    fleet.giveBack(contract.plate(), inspection.point(), inspection.mileage(), inspection.isDamaged(), now.toLocalDate());
    contract.attachInvoice(cashier.finalInvoice(rentalReturn));
    contracts.save(contract);

    return contract;
  }

  public Contract contract(ContractId id) {
    return contracts.contract(id);
  }
}
