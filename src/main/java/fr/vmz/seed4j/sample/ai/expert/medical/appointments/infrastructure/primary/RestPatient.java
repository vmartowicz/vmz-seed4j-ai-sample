package fr.vmz.seed4j.sample.ai.expert.medical.appointments.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.Patient;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.Sex;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import java.time.LocalDate;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestPatient(
  UUID id,
  String lastName,
  String firstName,
  LocalDate birthDate,
  Sex sex,
  String socialSecurityNumber,
  String phone,
  @Nullable UUID referringDoctorId,
  UUID insuredId,
  boolean longTermIllness,
  boolean solidarityHealthCover
) {
  static RestPatient from(Patient patient) {
    return new RestPatient(
      patient.id().get(),
      patient.lastName(),
      patient.firstName(),
      patient.birthDate(),
      patient.sex(),
      patient.socialSecurityNumber().get(),
      patient.phone(),
      patient.referringDoctor().map(DoctorId::get).orElse(null),
      patient.insured().get(),
      patient.longTermIllness(),
      patient.solidarityHealthCover()
    );
  }
}
