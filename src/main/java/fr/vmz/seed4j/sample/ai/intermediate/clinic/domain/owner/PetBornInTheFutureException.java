package fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner;

import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.InvalidClinicValueException;
import java.time.LocalDate;

class PetBornInTheFutureException extends InvalidClinicValueException {

  PetBornInTheFutureException(LocalDate birthDate) {
    super("A pet can't be born in the future: " + birthDate);
  }
}
