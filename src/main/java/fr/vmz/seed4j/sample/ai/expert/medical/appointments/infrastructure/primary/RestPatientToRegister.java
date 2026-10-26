package fr.vmz.seed4j.sample.ai.expert.medical.appointments.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.PatientToRegister;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.Sex;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestPatientToRegister(
  @Nullable String lastName,
  @Nullable String firstName,
  @Nullable LocalDate birthDate,
  @Nullable Sex sex,
  @Nullable String socialSecurityNumber,
  @Nullable String phone,
  @Nullable UUID referringDoctorId,
  @Nullable UUID insuredId,
  @Nullable Boolean longTermIllness,
  @Nullable Boolean solidarityHealthCover
) {
  PatientToRegister toDomain() {
    return new PatientToRegister(
      lastName,
      firstName,
      birthDate,
      sex,
      socialSecurityNumber,
      phone,
      Optional.ofNullable(referringDoctorId).map(DoctorId::new).orElse(null),
      Optional.ofNullable(insuredId).map(PatientId::new).orElse(null),
      Boolean.TRUE.equals(longTermIllness),
      Boolean.TRUE.equals(solidarityHealthCover)
    );
  }
}
