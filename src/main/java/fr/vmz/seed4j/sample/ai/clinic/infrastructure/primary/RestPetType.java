package fr.vmz.seed4j.sample.ai.clinic.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.clinic.domain.owner.PetType;

enum RestPetType {
  CAT,
  DOG,
  LIZARD,
  SNAKE,
  BIRD,
  HAMSTER;

  static RestPetType from(PetType type) {
    return valueOf(type.name());
  }

  PetType toDomain() {
    return PetType.valueOf(name());
  }
}
