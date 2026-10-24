package fr.vmz.seed4j.sample.ai.clinic.domain.owner;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;

public record PetInformation(PetName name, LocalDate birthDate, PetType type) {
  public PetInformation {
    Assert.notNull("name", name);
    Assert.notNull("birthDate", birthDate);
    Assert.notNull("type", type);
  }
}
