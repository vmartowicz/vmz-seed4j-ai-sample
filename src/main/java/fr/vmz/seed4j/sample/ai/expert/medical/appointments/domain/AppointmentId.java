package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record AppointmentId(UUID id) {
  public AppointmentId {
    Assert.notNull("appointmentId", id);
  }

  public static AppointmentId newId() {
    return new AppointmentId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
