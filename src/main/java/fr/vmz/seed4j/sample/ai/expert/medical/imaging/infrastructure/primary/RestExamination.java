package fr.vmz.seed4j.sample.ai.expert.medical.imaging.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.Examination;
import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ExaminationStatus;
import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ImagingExam;
import java.time.LocalDateTime;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestExamination(
  UUID id,
  UUID patientId,
  UUID prescriptionId,
  UUID prescriberId,
  ImagingExam exam,
  LocalDateTime performedAt,
  boolean imagesAvailable,
  ExaminationStatus status,
  @Nullable String report,
  @Nullable LocalDateTime reportedAt
) {
  static RestExamination from(Examination examination) {
    return new RestExamination(
      examination.id().get(),
      examination.patient().get(),
      examination.prescription(),
      examination.prescriber().get(),
      examination.exam(),
      examination.performedAt(),
      examination.imagesAvailable(),
      examination.status(),
      examination.report().orElse(null),
      examination.reportedAt().orElse(null)
    );
  }
}
