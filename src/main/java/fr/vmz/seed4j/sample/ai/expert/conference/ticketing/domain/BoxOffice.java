package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

public class BoxOffice {

  private static final int CAPACITY = 1200;

  private final TicketsRepository tickets;
  private final WorkshopRostersRepository rosters;
  private final Workshops workshops;

  public BoxOffice(TicketsRepository tickets, WorkshopRostersRepository rosters, Workshops workshops) {
    this.tickets = tickets;
    this.rosters = rosters;
    this.workshops = workshops;
  }

  public PlacedOrder order(OrderToPlace order, LocalDate today) {
    Assert.notNull("order", order);

    var valid = tickets.all().stream().filter(Ticket::isValid).toList();
    var size = order.attendees().size();
    if (valid.size() + size > CAPACITY) {
      throw new SoldOutException(CAPACITY);
    }

    var earlyBirds = valid.stream().filter(ticket -> ticket.type() == TicketType.EARLY_BIRD).count();
    var sold = new ArrayList<Ticket>();
    for (var attendee : order.attendees()) {
      var type = TicketPrices.typeFor(attendee, today, earlyBirds);
      if (type == TicketType.EARLY_BIRD) {
        earlyBirds++;
      }

      sold.add(Ticket.sold(type, attendee.holder(), TicketPrices.priceOf(type, size)));
    }
    sold.forEach(tickets::save);

    return new PlacedOrder(UUID.randomUUID(), order.buyer(), List.copyOf(sold));
  }

  public Ticket issueSpeakerTicket(Holder speaker) {
    var held = tickets.all().stream().filter(Ticket::isValid).filter(ticket -> ticket.isHeldBy(speaker)).toList();

    var existing = held.stream().filter(ticket -> ticket.type() == TicketType.SPEAKER).findFirst();
    if (existing.isPresent()) {
      return existing.orElseThrow();
    }

    held
      .stream()
      .filter(ticket -> ticket.type().isSold())
      .forEach(bought -> {
        bought.replaceBySpeakerTicket();
        tickets.save(bought);
      });

    var ticket = Ticket.free(TicketType.SPEAKER, speaker);
    tickets.save(ticket);
    return ticket;
  }

  public void revokeSpeakerTicket(Email speaker) {
    tickets
      .all()
      .stream()
      .filter(Ticket::isValid)
      .filter(ticket -> ticket.type() == TicketType.SPEAKER && ticket.holder().email().equals(speaker))
      .forEach(ticket -> {
        ticket.cancel();
        tickets.save(ticket);
      });
  }

  public List<Ticket> issueSponsorTickets(Holder contact, int count) {
    var issued = IntStream.range(0, count).mapToObj(index -> Ticket.free(TicketType.SPONSOR, contact)).toList();
    issued.forEach(tickets::save);

    return issued;
  }

  public Ticket rename(TicketNumber number, Holder holder, LocalDate today) {
    var ticket = tickets.ticket(number);

    ticket.rename(holder, today);
    tickets.save(ticket);

    return ticket;
  }

  public Ticket refund(TicketNumber number, LocalDate today) {
    var ticket = tickets.ticket(number);

    ticket.refund(today);
    tickets.save(ticket);

    return ticket;
  }

  public Ticket ticket(TicketNumber number) {
    return tickets.ticket(number);
  }

  public Optional<Ticket> findTicket(TicketNumber number) {
    return tickets.get(number);
  }

  public List<Ticket> ticketsOf(Email holder) {
    return tickets.all().stream().filter(ticket -> ticket.holder().email().equals(holder)).toList();
  }

  public WorkshopRegistration register(UUID workshop, TicketNumber number, LocalDateTime now) {
    var ticket = tickets.ticket(number);
    if (!ticket.isValid()) {
      throw new TicketNotValidException(number, ticket.status());
    }

    if (!workshops.exists(workshop)) {
      throw new UnknownWorkshopException(workshop);
    }

    var roster = rosters.get(workshop).orElseGet(() -> new WorkshopRoster(workshop));
    var registration = roster.register(number, now);
    rosters.save(roster);

    return registration;
  }

  public void cancelRegistration(UUID workshop, TicketNumber ticket) {
    var roster = rosters.get(workshop).orElseThrow(() -> new NotRegisteredException(ticket, workshop));

    roster.cancel(ticket);
    rosters.save(roster);
  }

  public List<WorkshopRegistration> roster(UUID workshop) {
    return rosters.get(workshop).map(WorkshopRoster::registrations).orElse(List.of());
  }

  public boolean isRegistered(UUID workshop, TicketNumber ticket) {
    return rosters.get(workshop).map(roster -> roster.isRegistered(ticket)).orElse(false);
  }
}
