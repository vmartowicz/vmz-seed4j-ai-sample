package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.application;

import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.BoxOffice;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.Holder;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.OrderToPlace;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.PlacedOrder;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.Ticket;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.TicketNumber;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.TicketsRepository;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.WorkshopRegistration;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.WorkshopRostersRepository;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.Workshops;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TicketingApplicationService {

  private final BoxOffice office;
  private final Clock clock;

  public TicketingApplicationService(TicketsRepository tickets, WorkshopRostersRepository rosters, Workshops workshops, Clock clock) {
    office = new BoxOffice(tickets, rosters, workshops);
    this.clock = clock;
  }

  public PlacedOrder order(OrderToPlace order) {
    return office.order(order, LocalDate.now(clock));
  }

  public Ticket ticket(TicketNumber ticket) {
    return office.ticket(ticket);
  }

  public Optional<Ticket> findTicket(TicketNumber ticket) {
    return office.findTicket(ticket);
  }

  public boolean isRegistered(UUID workshop, TicketNumber ticket) {
    return office.isRegistered(workshop, ticket);
  }

  public List<Ticket> ticketsOf(Email holder) {
    return office.ticketsOf(holder);
  }

  public Ticket rename(TicketNumber ticket, Holder holder) {
    return office.rename(ticket, holder, LocalDate.now(clock));
  }

  public Ticket refund(TicketNumber ticket) {
    return office.refund(ticket, LocalDate.now(clock));
  }

  public Ticket issueSpeakerTicket(Holder speaker) {
    return office.issueSpeakerTicket(speaker);
  }

  public void revokeSpeakerTicket(Email speaker) {
    office.revokeSpeakerTicket(speaker);
  }

  public List<Ticket> issueSponsorTickets(Holder contact, int count) {
    return office.issueSponsorTickets(contact, count);
  }

  public WorkshopRegistration register(UUID workshop, TicketNumber ticket) {
    return office.register(workshop, ticket, LocalDateTime.now(clock));
  }

  public void cancelRegistration(UUID workshop, TicketNumber ticket) {
    office.cancelRegistration(workshop, ticket);
  }

  public List<WorkshopRegistration> roster(UUID workshop) {
    return office.roster(workshop);
  }
}
