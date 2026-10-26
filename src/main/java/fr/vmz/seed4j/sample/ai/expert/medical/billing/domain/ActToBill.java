package fr.vmz.seed4j.sample.ai.expert.medical.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDate;
import java.util.UUID;

public record ActToBill(UUID consultation, PatientId patient, DoctorId doctor, LocalDate date) {
  public ActToBill {
    Assert.notNull("consultation", consultation);
    Assert.notNull("patient", patient);
    Assert.notNull("doctor", doctor);
    Assert.notNull("date", date);
  }
}
