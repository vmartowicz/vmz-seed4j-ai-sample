package fr.vmz.seed4j.sample.ai.expert.medical.records.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public final class RecordsFixture {

  public static final LocalDateTime NOW = LocalDateTime.of(2026, 6, 1, 10, 0);
  public static final LocalDate TODAY = NOW.toLocalDate();

  private RecordsFixture() {}

  public static Consultation openConsultation() {
    return Consultation.open(PatientId.newId(), DoctorId.newId(), "Sore throat", NOW);
  }

  public static PrescriptionItem medicine(String name) {
    return new PrescriptionItem(ItemKind.MEDICINE, name);
  }

  public static List<PrescriptionItem> items(PrescriptionItem... items) {
    return List.of(items);
  }
}
