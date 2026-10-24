package fr.vmz.seed4j.sample.ai.clinic.domain.owner;

import fr.vmz.seed4j.sample.ai.clinic.domain.UnknownClinicElementException;

public class UnknownOwnerException extends UnknownClinicElementException {

  public UnknownOwnerException(OwnerId owner) {
    super("Unknown owner " + owner.get());
  }
}
