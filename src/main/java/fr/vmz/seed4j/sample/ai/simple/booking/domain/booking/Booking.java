package fr.vmz.seed4j.sample.ai.simple.booking.domain.booking;

import fr.vmz.seed4j.sample.ai.simple.booking.domain.Slot;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.ServiceId;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class Booking {

  private final BookingId id;
  private final ServiceId service;
  private final Customer customer;
  private Slot slot;
  private BookingStatus status = BookingStatus.CONFIRMED;
  private boolean reminderSent;

  Booking(BookingId id, BookingToCreate booking) {
    this.id = id;
    service = booking.service();
    customer = booking.customer();
    slot = booking.slot();
  }

  public void changeSlot(Slot newSlot, LocalDateTime now) {
    Assert.notNull("newSlot", newSlot);
    assertModifiable(now);

    slot = newSlot;
    reminderSent = false;
  }

  public void cancel(LocalDateTime now) {
    assertModifiable(now);

    status = BookingStatus.CANCELLED;
  }

  private void assertModifiable(LocalDateTime now) {
    Assert.notNull("now", now);

    if (status == BookingStatus.CANCELLED) {
      throw new BookingCancelledException(id);
    }

    if (!now.isBefore(slot.start())) {
      throw new BookingStartedException(id);
    }
  }

  public boolean needsReminderOn(LocalDate day) {
    return status == BookingStatus.CONFIRMED && !reminderSent && slot.start().toLocalDate().equals(day);
  }

  public void markReminderSent() {
    reminderSent = true;
  }

  public BookingId id() {
    return id;
  }

  public ServiceId service() {
    return service;
  }

  public Customer customer() {
    return customer;
  }

  public Slot slot() {
    return slot;
  }

  public BookingStatus status() {
    return status;
  }

  public boolean reminderSent() {
    return reminderSent;
  }
}
