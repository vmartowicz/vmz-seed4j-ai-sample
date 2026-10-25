package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record RoomBookingId(UUID id) {
  public RoomBookingId {
    Assert.notNull("id", id);
  }

  public static RoomBookingId newId() {
    return new RoomBookingId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
