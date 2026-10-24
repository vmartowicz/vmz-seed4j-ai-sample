package fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog;

import fr.vmz.seed4j.sample.ai.simple.booking.domain.UnknownBookingElementException;

public class UnknownServiceException extends UnknownBookingElementException {

  UnknownServiceException(ServiceId service) {
    super("Unknown service " + service.get());
  }
}
