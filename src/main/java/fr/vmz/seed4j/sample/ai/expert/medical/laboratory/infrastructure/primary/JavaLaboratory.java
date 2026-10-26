package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.application.LaboratoryApplicationService;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class JavaLaboratory {

  private final LaboratoryApplicationService laboratory;

  public JavaLaboratory(LaboratoryApplicationService laboratory) {
    this.laboratory = laboratory;
  }

  public boolean hasCreatinineSince(UUID patient, LocalDate since) {
    return laboratory.hasCreatinineSince(new PatientId(patient), since);
  }
}
