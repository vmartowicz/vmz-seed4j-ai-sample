package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class AppointmentsFixture {

  public static final LocalDate MONDAY = LocalDate.of(2026, 6, 1);
  public static final LocalDateTime NOW = MONDAY.atTime(8, 0);
  public static final String SSN = "1900575123456" + "22";

  private AppointmentsFixture() {}

  public static Doctor generalPractitioner() {
    return new DoctorToRegister("Dr House", DoctorKind.GENERAL_PRACTITIONER, Centre.LYON_7, BigDecimal.ZERO, false).register();
  }

  public static Doctor specialist() {
    return new DoctorToRegister("Dr Strange", DoctorKind.SPECIALIST, Centre.VENISSIEUX, new BigDecimal("20"), false).register();
  }

  public static PatientToRegister adult() {
    return new PatientToRegister("Doe", "Jane", LocalDate.of(1990, 5, 4), Sex.FEMALE, SSN, "0612345678", null, null, false, false);
  }

  public static PatientToRegister child(LocalDate birthDate) {
    return new PatientToRegister("Doe", "Kid", birthDate, Sex.MALE, SSN, "0612345678", null, null, false, false);
  }

  public static Patient patient() {
    return adult().register();
  }
}
