package fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner;

import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.InvalidClinicValueException;
import java.time.LocalDate;

class VisitNotInFutureException extends InvalidClinicValueException {

  VisitNotInFutureException(LocalDate date) {
    super("A visit must be scheduled from tomorrow, got: " + date);
  }
}
