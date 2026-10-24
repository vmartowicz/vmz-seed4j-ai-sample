package fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner;

import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.UnknownClinicElementException;

public class UnknownOwnerException extends UnknownClinicElementException {

  public UnknownOwnerException(OwnerId owner) {
    super("Unknown owner " + owner.get());
  }
}
