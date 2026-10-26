package fr.vmz.seed4j.sample.ai.expert.medical.records.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDate;
import java.util.List;

public final class Prescription {

  private final PrescriptionId id;
  private final PatientId patient;
  private final DoctorId prescriber;
  private final LocalDate issuedOn;
  private final List<PrescriptionItem> items;

  Prescription(PatientId patient, DoctorId prescriber, LocalDate issuedOn, List<PrescriptionItem> items) {
    id = PrescriptionId.newId();
    this.patient = patient;
    this.prescriber = prescriber;
    this.issuedOn = issuedOn;
    this.items = List.copyOf(items);
  }

  public boolean isValidFor(ItemKind kind, String code, LocalDate date) {
    return items.stream().anyMatch(item -> item.is(kind, code)) && !date.isAfter(validUntil(kind));
  }

  public LocalDate validUntil(ItemKind kind) {
    return issuedOn.plus(kind.validity());
  }

  public PrescriptionId id() {
    return id;
  }

  public PatientId patient() {
    return patient;
  }

  public DoctorId prescriber() {
    return prescriber;
  }

  public LocalDate issuedOn() {
    return issuedOn;
  }

  public List<PrescriptionItem> items() {
    return items;
  }
}
