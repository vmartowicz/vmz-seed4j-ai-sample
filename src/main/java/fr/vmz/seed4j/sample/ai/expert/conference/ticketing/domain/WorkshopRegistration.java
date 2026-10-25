package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

import java.time.LocalDateTime;

public final class WorkshopRegistration {

  private final TicketNumber ticket;
  private final LocalDateTime requestedAt;
  private RegistrationStatus status;

  WorkshopRegistration(TicketNumber ticket, LocalDateTime requestedAt, RegistrationStatus status) {
    this.ticket = ticket;
    this.requestedAt = requestedAt;
    this.status = status;
  }

  void promote() {
    status = RegistrationStatus.REGISTERED;
  }

  void cancel() {
    status = RegistrationStatus.CANCELLED;
  }

  boolean isActive() {
    return status != RegistrationStatus.CANCELLED;
  }

  public TicketNumber ticket() {
    return ticket;
  }

  public LocalDateTime requestedAt() {
    return requestedAt;
  }

  public RegistrationStatus status() {
    return status;
  }
}
