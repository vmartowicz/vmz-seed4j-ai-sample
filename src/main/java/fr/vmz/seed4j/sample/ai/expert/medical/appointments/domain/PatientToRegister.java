package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDate;
import org.jspecify.annotations.Nullable;

public record PatientToRegister(
  String lastName,
  String firstName,
  LocalDate birthDate,
  Sex sex,
  String socialSecurityNumber,
  String phone,
  @Nullable DoctorId referringDoctor,
  @Nullable PatientId insured,
  boolean longTermIllness,
  boolean solidarityHealthCover
) {
  public PatientToRegister {
    Assert.field("lastName", lastName).notBlank().maxLength(100);
    Assert.field("firstName", firstName).notBlank().maxLength(100);
    Assert.notNull("birthDate", birthDate);
    Assert.notNull("sex", sex);
    Assert.field("phone", phone).notBlank().maxLength(20);
  }

  public Patient register() {
    return new Patient(this);
  }
}
