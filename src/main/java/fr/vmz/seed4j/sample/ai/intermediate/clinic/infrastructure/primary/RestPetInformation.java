package fr.vmz.seed4j.sample.ai.intermediate.clinic.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.PetInformation;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.PetName;
import java.time.LocalDate;
import org.jspecify.annotations.Nullable;

record RestPetInformation(@Nullable String name, @Nullable LocalDate birthDate, @Nullable RestPetType type) {
  PetInformation toDomain() {
    return new PetInformation(new PetName(name), birthDate, type == null ? null : type.toDomain());
  }
}
