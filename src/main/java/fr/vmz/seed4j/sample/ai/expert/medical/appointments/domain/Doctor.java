package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import java.math.BigDecimal;

public final class Doctor {

  private final DoctorId id;
  private final String name;
  private final DoctorKind kind;
  private final Centre centre;
  private final BigDecimal feeOverrun;
  private final boolean onCall;

  Doctor(DoctorToRegister doctor) {
    id = DoctorId.newId();
    name = doctor.name().trim();
    kind = doctor.kind();
    centre = doctor.centre();
    feeOverrun = doctor.feeOverrun();
    onCall = doctor.onCall();
  }

  public DoctorId id() {
    return id;
  }

  public String name() {
    return name;
  }

  public DoctorKind kind() {
    return kind;
  }

  public Centre centre() {
    return centre;
  }

  public BigDecimal feeOverrun() {
    return feeOverrun;
  }

  public boolean onCall() {
    return onCall;
  }
}
