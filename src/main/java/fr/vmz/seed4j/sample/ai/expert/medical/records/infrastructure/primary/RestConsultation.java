package fr.vmz.seed4j.sample.ai.expert.medical.records.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.Consultation;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.ConsultationStatus;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.PrescriptionId;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestConsultation(
  UUID id,
  UUID patientId,
  UUID doctorId,
  String reason,
  LocalDateTime openedAt,
  @Nullable String examination,
  @Nullable String diagnosis,
  @Nullable UUID prescriptionId,
  ConsultationStatus status,
  @Nullable LocalDateTime closedAt,
  List<RestAddendum> addenda
) {
  static RestConsultation from(Consultation consultation) {
    return new RestConsultation(
      consultation.id().get(),
      consultation.patient().get(),
      consultation.doctor().get(),
      consultation.reason(),
      consultation.openedAt(),
      consultation.examination().orElse(null),
      consultation.diagnosis().orElse(null),
      consultation.prescription().map(PrescriptionId::get).orElse(null),
      consultation.status(),
      consultation.closedAt().orElse(null),
      consultation.addenda().stream().map(RestAddendum::from).toList()
    );
  }
}
