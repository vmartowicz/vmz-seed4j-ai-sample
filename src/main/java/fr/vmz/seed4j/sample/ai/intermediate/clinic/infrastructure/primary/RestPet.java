package fr.vmz.seed4j.sample.ai.intermediate.clinic.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.Pet;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

record RestPet(UUID id, String name, LocalDate birthDate, RestPetType type, List<RestVisit> visits) {
  static RestPet from(Pet pet) {
    return new RestPet(
      pet.id().get(),
      pet.name().get(),
      pet.birthDate(),
      RestPetType.from(pet.type()),
      pet.visits().stream().map(RestVisit::from).toList()
    );
  }
}
