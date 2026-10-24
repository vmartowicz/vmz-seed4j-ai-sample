package fr.vmz.seed4j.sample.ai.clinic.domain.owner;

import fr.vmz.seed4j.sample.ai.clinic.domain.InvalidClinicValueException;
import java.time.LocalDate;

class PetBornInTheFutureException extends InvalidClinicValueException {

  PetBornInTheFutureException(LocalDate birthDate) {
    super("A pet can't be born in the future: " + birthDate);
  }
}
