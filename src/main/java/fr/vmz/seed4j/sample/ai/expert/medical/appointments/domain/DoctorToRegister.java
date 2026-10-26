package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigDecimal;
import org.jspecify.annotations.Nullable;

public record DoctorToRegister(String name, DoctorKind kind, Centre centre, @Nullable BigDecimal feeOverrun, boolean onCall) {
  public DoctorToRegister {
    Assert.field("name", name).notBlank().maxLength(100);
    Assert.notNull("kind", kind);
    Assert.notNull("centre", centre);

    feeOverrun = feeOverrun == null ? BigDecimal.ZERO : feeOverrun;
    Assert.field("feeOverrun", feeOverrun).min(0);
  }

  public Doctor register() {
    return new Doctor(this);
  }
}
