package fr.vmz.seed4j.sample.ai.expert.medical.appointments.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.Centre;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.Doctor;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.DoctorKind;
import java.math.BigDecimal;
import java.util.UUID;

record RestDoctor(UUID id, String name, DoctorKind kind, Centre centre, BigDecimal feeOverrun, boolean onCall) {
  static RestDoctor from(Doctor doctor) {
    return new RestDoctor(doctor.id().get(), doctor.name(), doctor.kind(), doctor.centre(), doctor.feeOverrun(), doctor.onCall());
  }
}
