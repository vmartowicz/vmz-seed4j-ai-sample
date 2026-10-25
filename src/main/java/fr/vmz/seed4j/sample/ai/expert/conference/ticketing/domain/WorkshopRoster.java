package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public final class WorkshopRoster {

  private static final int SEATS = 30;

  private final UUID workshop;
  private final List<WorkshopRegistration> registrations = new ArrayList<>();

  public WorkshopRoster(UUID workshop) {
    Assert.notNull("workshop", workshop);

    this.workshop = workshop;
  }

  public WorkshopRegistration register(TicketNumber ticket, LocalDateTime now) {
    if (active(ticket).isPresent()) {
      throw new AlreadyRegisteredException(ticket, workshop);
    }

    var full = registered().count() >= SEATS;
    var registration = new WorkshopRegistration(ticket, now, full ? RegistrationStatus.WAITING : RegistrationStatus.REGISTERED);
    registrations.add(registration);

    return registration;
  }

  public Optional<WorkshopRegistration> cancel(TicketNumber ticket) {
    var registration = active(ticket).orElseThrow(() -> new NotRegisteredException(ticket, workshop));
    var freesSeat = registration.status() == RegistrationStatus.REGISTERED;

    registration.cancel();
    if (!freesSeat) {
      return Optional.empty();
    }

    var next = registrations
      .stream()
      .filter(waiting -> waiting.status() == RegistrationStatus.WAITING)
      .min(Comparator.comparing(WorkshopRegistration::requestedAt));
    next.ifPresent(WorkshopRegistration::promote);

    return next;
  }

  public boolean isRegistered(TicketNumber ticket) {
    return registered().anyMatch(registration -> registration.ticket().equals(ticket));
  }

  private Stream<WorkshopRegistration> registered() {
    return registrations.stream().filter(registration -> registration.status() == RegistrationStatus.REGISTERED);
  }

  private Optional<WorkshopRegistration> active(TicketNumber ticket) {
    return registrations
      .stream()
      .filter(WorkshopRegistration::isActive)
      .filter(registration -> registration.ticket().equals(ticket))
      .findFirst();
  }

  public UUID workshop() {
    return workshop;
  }

  public List<WorkshopRegistration> registrations() {
    return List.copyOf(registrations);
  }
}
