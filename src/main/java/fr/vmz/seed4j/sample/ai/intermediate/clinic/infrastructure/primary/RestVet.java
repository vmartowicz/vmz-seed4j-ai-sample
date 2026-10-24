package fr.vmz.seed4j.sample.ai.intermediate.clinic.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.vet.Specialty;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.vet.Vet;
import java.util.List;
import java.util.UUID;

record RestVet(UUID id, String firstName, String lastName, List<String> specialties) {
  static RestVet from(Vet vet) {
    return new RestVet(vet.id().get(), vet.firstName().get(), vet.lastName().get(), vet.specialties().stream().map(Specialty::name).toList());
  }
}
