package fr.vmz.seed4j.sample.ai.expert.medical.imaging.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ExamToPerform;
import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ImagingExam;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestExamToPerform(@Nullable UUID patientId, @Nullable UUID prescriptionId, @Nullable ImagingExam exam, @Nullable Boolean negativePregnancyTest) {
  ExamToPerform toDomain() {
    return new ExamToPerform(new PatientId(patientId), prescriptionId, exam, Boolean.TRUE.equals(negativePregnancyTest));
  }
}
