package fr.vmz.seed4j.sample.ai.expert.medical.imaging.infrastructure.secondary.laboratory;

import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.KidneyFunction;
import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.infrastructure.primary.JavaLaboratory;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
class ImagingKidneyFunction implements KidneyFunction {

  private final JavaLaboratory laboratory;

  ImagingKidneyFunction(JavaLaboratory laboratory) {
    this.laboratory = laboratory;
  }

  @Override
  public boolean hasCreatinineSince(PatientId patient, LocalDate since) {
    return laboratory.hasCreatinineSince(patient.get(), since);
  }
}
