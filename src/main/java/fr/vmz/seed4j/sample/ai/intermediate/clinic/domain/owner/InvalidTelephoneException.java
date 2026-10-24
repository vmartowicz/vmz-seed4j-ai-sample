package fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner;

import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.InvalidClinicValueException;

class InvalidTelephoneException extends InvalidClinicValueException {

  InvalidTelephoneException(String telephone) {
    super("Telephone must have exactly 10 digits, got: " + telephone);
  }
}
