package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

public enum DoctorKind {
  GENERAL_PRACTITIONER(15),
  SPECIALIST(30);

  private final int slotMinutes;

  DoctorKind(int slotMinutes) {
    this.slotMinutes = slotMinutes;
  }

  int slotMinutes() {
    return slotMinutes;
  }
}
