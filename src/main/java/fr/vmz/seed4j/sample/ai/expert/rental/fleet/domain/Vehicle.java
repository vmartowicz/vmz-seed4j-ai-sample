package fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain;

import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.time.LocalDate;

public final class Vehicle {

  private static final int SERVICE_KILOMETRES = 15_000;
  private static final int SERVICE_MONTHS = 12;
  private static final int MAX_KILOMETRES = 150_000;
  private static final int MAX_YEARS = 5;

  private final LicencePlate plate;
  private final String make;
  private final String model;
  private final Category category;
  private final FuelType fuel;
  private final LocalDate firstRegistration;
  private int mileage;
  private RentalPoint point;
  private LocalDate lastServiceDate;
  private int lastServiceMileage;
  private VehicleStatus status;

  Vehicle(VehicleToRegister vehicle) {
    plate = vehicle.plate();
    make = vehicle.make().trim();
    model = vehicle.model().trim();
    category = vehicle.category();
    fuel = vehicle.fuel();
    firstRegistration = vehicle.firstRegistration();
    mileage = vehicle.mileage();
    point = vehicle.point();
    lastServiceDate = vehicle.lastService();
    lastServiceMileage = vehicle.mileage();
    status = VehicleStatus.AVAILABLE;
  }

  public boolean isRentableAt(RentalPoint at, LocalDate date) {
    return status == VehicleStatus.AVAILABLE && point == at && !isServiceDueOn(date) && !isRetiredOn(date);
  }

  public boolean isServiceDueOn(LocalDate date) {
    return mileage >= nextServiceMileage() || !date.isBefore(nextServiceOn());
  }

  public void pickUp() {
    assertStatus(VehicleStatus.AVAILABLE, "pick up");

    status = VehicleStatus.RENTED;
  }

  public void giveBack(RentalPoint returnPoint, int returnMileage, boolean damaged, LocalDate today) {
    assertStatus(VehicleStatus.RENTED, "give back");
    if (returnMileage < mileage) {
      throw new InvalidMileageException(plate, mileage);
    }

    mileage = returnMileage;
    point = returnPoint;
    status = statusAfterUse(damaged || isServiceDueOn(today), today);
  }

  public void completeMaintenance(LocalDate today) {
    if (status != VehicleStatus.IN_MAINTENANCE && status != VehicleStatus.AVAILABLE) {
      throw new InvalidVehicleTransitionException(plate, status, "service");
    }

    lastServiceDate = today;
    lastServiceMileage = mileage;
    status = statusAfterUse(false, today);
  }

  public void transferTo(RentalPoint destination) {
    assertStatus(VehicleStatus.AVAILABLE, "transfer");

    point = destination;
  }

  public LocalDate nextServiceOn() {
    return lastServiceDate.plusMonths(SERVICE_MONTHS);
  }

  public int nextServiceMileage() {
    return lastServiceMileage + SERVICE_KILOMETRES;
  }

  private VehicleStatus statusAfterUse(boolean needsMaintenance, LocalDate today) {
    if (isRetiredOn(today)) {
      return VehicleStatus.OUT_OF_SERVICE;
    }

    return needsMaintenance ? VehicleStatus.IN_MAINTENANCE : VehicleStatus.AVAILABLE;
  }

  private boolean isRetiredOn(LocalDate date) {
    return mileage > MAX_KILOMETRES || date.isAfter(firstRegistration.plusYears(MAX_YEARS));
  }

  private void assertStatus(VehicleStatus expected, String action) {
    if (status != expected) {
      throw new InvalidVehicleTransitionException(plate, status, action);
    }
  }

  public LicencePlate plate() {
    return plate;
  }

  public String make() {
    return make;
  }

  public String model() {
    return model;
  }

  public Category category() {
    return category;
  }

  public FuelType fuel() {
    return fuel;
  }

  public int mileage() {
    return mileage;
  }

  public RentalPoint point() {
    return point;
  }

  public VehicleStatus status() {
    return status;
  }
}
