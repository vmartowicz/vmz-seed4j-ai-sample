package fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner;

import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.ClinicRuleViolationException;

class DuplicatedPetNameException extends ClinicRuleViolationException {

  DuplicatedPetNameException(PetName name) {
    super("The owner already has a pet named " + name.get());
  }
}
