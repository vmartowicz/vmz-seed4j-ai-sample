package fr.vmz.seed4j.sample.ai.clinic.domain.owner;

import fr.vmz.seed4j.sample.ai.clinic.domain.UnknownClinicElementException;

class UnknownPetException extends UnknownClinicElementException {

  UnknownPetException(PetId pet) {
    super("The owner has no pet " + pet.get());
  }
}
