package fr.vmz.seed4j.sample.ai.expert.rental.counter.domain;

import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public final class Contract {

  private final ContractId id;
  private final BookedRental booked;
  private final RentedVehicle vehicle;
  private final LocalDateTime pickedUpAt;
  private final BigDecimal deposit;
  private ContractStatus status;
  private @Nullable LocalDateTime returnedAt;
  private @Nullable ReturnInspection returnInspection;
  private @Nullable UUID invoice;

  private Contract(BookedRental booked, RentedVehicle vehicle, LocalDateTime pickedUpAt) {
    id = ContractId.newId();
    this.booked = booked;
    this.vehicle = vehicle;
    this.pickedUpAt = pickedUpAt;
    deposit = VehicleChoice.depositOf(booked.category());
    status = ContractStatus.OPEN;
  }

  public static Contract open(BookedRental booked, RentedVehicle vehicle, LocalDateTime pickedUpAt) {
    return new Contract(booked, vehicle, pickedUpAt);
  }

  public RentalReturn close(ReturnInspection inspection, LocalDateTime now) {
    if (status == ContractStatus.CLOSED) {
      throw new ContractAlreadyClosedException(id);
    }

    if (inspection.mileage() < vehicle.mileage()) {
      throw new InvalidReturnMileageException(vehicle.mileage(), inspection.mileage());
    }

    status = ContractStatus.CLOSED;
    returnedAt = now;
    returnInspection = inspection;

    return new RentalReturn(
      id,
      booked.customer(),
      booked.category(),
      booked.dailyRate(),
      booked.days(),
      booked.paid(),
      pickedUpAt,
      booked.returnAt(),
      now,
      booked.pickUpPoint(),
      inspection.point(),
      inspection.mileage() - vehicle.mileage(),
      inspection.missingFuel(),
      vehicle.electric(),
      inspection.isDamaged()
    );
  }

  public void attachInvoice(UUID finalInvoice) {
    invoice = finalInvoice;
  }

  public ContractId id() {
    return id;
  }

  public UUID reservation() {
    return booked.reservation();
  }

  public Email customer() {
    return booked.customer();
  }

  public Category bookedCategory() {
    return booked.category();
  }

  public String plate() {
    return vehicle.plate();
  }

  public Category vehicleCategory() {
    return vehicle.category();
  }

  public RentalPoint pickUpPoint() {
    return booked.pickUpPoint();
  }

  public LocalDateTime pickedUpAt() {
    return pickedUpAt;
  }

  public LocalDateTime plannedReturnAt() {
    return booked.returnAt();
  }

  public int mileageOut() {
    return vehicle.mileage();
  }

  public BigDecimal deposit() {
    return deposit;
  }

  public ContractStatus status() {
    return status;
  }

  public Optional<LocalDateTime> returnedAt() {
    return Optional.ofNullable(returnedAt);
  }

  public Optional<ReturnInspection> returnInspection() {
    return Optional.ofNullable(returnInspection);
  }

  public Optional<UUID> invoice() {
    return Optional.ofNullable(invoice);
  }
}
