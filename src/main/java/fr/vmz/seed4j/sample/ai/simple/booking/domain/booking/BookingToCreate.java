package fr.vmz.seed4j.sample.ai.simple.booking.domain.booking;

import fr.vmz.seed4j.sample.ai.simple.booking.domain.Slot;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.ServiceId;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record BookingToCreate(ServiceId service, Customer customer, Slot slot) {
  public BookingToCreate {
    Assert.notNull("service", service);
    Assert.notNull("customer", customer);
    Assert.notNull("slot", slot);
  }

  public Booking create() {
    return new Booking(BookingId.newId(), this);
  }
}
