package fr.vmz.seed4j.sample.ai.expert.medical.records.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

public final class Consultation {

  private final ConsultationId id;
  private final PatientId patient;
  private final DoctorId doctor;
  private final String reason;
  private final LocalDateTime openedAt;
  private final List<Addendum> addenda = new ArrayList<>();
  private @Nullable String examination;
  private @Nullable String diagnosis;
  private @Nullable PrescriptionId prescription;
  private @Nullable LocalDateTime closedAt;

  private Consultation(PatientId patient, DoctorId doctor, String reason, LocalDateTime openedAt) {
    Assert.notNull("patient", patient);
    Assert.notNull("doctor", doctor);
    Assert.notBlank("reason", reason);
    Assert.notNull("openedAt", openedAt);

    id = ConsultationId.newId();
    this.patient = patient;
    this.doctor = doctor;
    this.reason = reason;
    this.openedAt = openedAt;
  }

  public static Consultation open(PatientId patient, DoctorId doctor, String reason, LocalDateTime now) {
    return new Consultation(patient, doctor, reason, now);
  }

  public void write(String examination, String diagnosis) {
    assertOpen();

    this.examination = examination;
    this.diagnosis = diagnosis;
  }

  public Prescription prescribe(List<PrescriptionItem> items, MedicalRecord record, LocalDate today) {
    assertOpen();
    Assert.notEmpty("items", items);
    items
      .stream()
      .filter(item -> item.kind() == ItemKind.MEDICINE && record.isAllergicTo(item.code()))
      .findFirst()
      .ifPresent(item -> {
        throw new AllergyException(item.code());
      });

    var created = new Prescription(patient, doctor, today, items);
    prescription = created.id();

    return created;
  }

  public void close(LocalDateTime now) {
    assertOpen();

    closedAt = now;
  }

  public void addAddendum(DoctorId author, String text, LocalDateTime now) {
    if (closedAt == null) {
      throw new ConsultationOpenException(id);
    }

    addenda.add(new Addendum(author, text, now));
  }

  private void assertOpen() {
    if (closedAt != null) {
      throw new ConsultationClosedException(id);
    }
  }

  public ConsultationId id() {
    return id;
  }

  public PatientId patient() {
    return patient;
  }

  public DoctorId doctor() {
    return doctor;
  }

  public String reason() {
    return reason;
  }

  public LocalDateTime openedAt() {
    return openedAt;
  }

  public Optional<String> examination() {
    return Optional.ofNullable(examination);
  }

  public Optional<String> diagnosis() {
    return Optional.ofNullable(diagnosis);
  }

  public Optional<PrescriptionId> prescription() {
    return Optional.ofNullable(prescription);
  }

  public Optional<LocalDateTime> closedAt() {
    return Optional.ofNullable(closedAt);
  }

  public ConsultationStatus status() {
    return closedAt == null ? ConsultationStatus.OPEN : ConsultationStatus.CLOSED;
  }

  public List<Addendum> addenda() {
    return List.copyOf(addenda);
  }
}
