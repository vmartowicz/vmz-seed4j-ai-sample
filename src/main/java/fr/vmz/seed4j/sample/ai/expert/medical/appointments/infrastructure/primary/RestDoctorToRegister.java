package fr.vmz.seed4j.sample.ai.expert.medical.appointments.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.Centre;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.DoctorKind;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.DoctorToRegister;
import java.math.BigDecimal;
import org.jspecify.annotations.Nullable;

record RestDoctorToRegister(@Nullable String name, @Nullable DoctorKind kind, @Nullable Centre centre, @Nullable BigDecimal feeOverrun, boolean onCall) {
  DoctorToRegister toDomain() {
    return new DoctorToRegister(name, kind, centre, feeOverrun, onCall);
  }
}
