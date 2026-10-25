package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

public enum BookingStatus {
  PENDING,
  CONFIRMED,
  CANCELLED,
  HONORED,
  NO_SHOW;

  boolean isOpen() {
    return this == PENDING || this == CONFIRMED;
  }

  boolean holdsTables() {
    return isOpen() || this == HONORED;
  }
}
