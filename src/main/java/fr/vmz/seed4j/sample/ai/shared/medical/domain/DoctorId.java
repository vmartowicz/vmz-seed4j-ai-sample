package fr.vmz.seed4j.sample.ai.shared.medical.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record DoctorId(UUID id) {
  public DoctorId {
    Assert.notNull("doctorId", id);
  }

  public static DoctorId newId() {
    return new DoctorId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
