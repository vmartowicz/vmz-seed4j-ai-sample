package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

import java.util.UUID;

class UnknownWorkshopException extends UnknownTicketingElementException {

  UnknownWorkshopException(UUID workshop) {
    super("Unknown workshop " + workshop);
  }
}
