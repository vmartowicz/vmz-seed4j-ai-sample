package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

import static fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.TicketsFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class BoxOfficeTest {

  private final TicketsRepository tickets = mock(TicketsRepository.class, CALLS_REAL_METHODS);
  private final WorkshopRostersRepository rosters = mock(WorkshopRostersRepository.class);
  private final Workshops workshops = mock(Workshops.class);
  private final BoxOffice office = new BoxOffice(tickets, rosters, workshops);

  private final List<Ticket> existing = new ArrayList<>();

  @BeforeEach
  void loadTickets() {
    doReturn(existing).when(tickets).all();
  }

  @Nested
  class Order {

    @Test
    void shouldSellEarlyBirdTicketsUntilApril30() {
      var order = office.order(order(attendee("jane")), EARLY);

      assertThat(order.id()).isNotNull();
      assertThat(order.buyer()).isEqualTo(new Email("buyer@company.com"));
      assertThat(order.tickets()).singleElement().satisfies(ticket -> {
        assertThat(ticket.type()).isEqualTo(TicketType.EARLY_BIRD);
        assertThat(ticket.price()).isEqualByComparingTo("249");
      });
      assertThat(order.total()).isEqualByComparingTo("249");
      verify(tickets).save(order.tickets().getFirst());
    }

    @Test
    void shouldSellStandardTicketsFromMay() {
      assertThat(office.order(order(attendee("jane")), LATE).tickets().getFirst().type()).isEqualTo(TicketType.STANDARD);
    }

    @Test
    void shouldSellStudentTicketsAt99Euros() {
      var ticket = office.order(order(student("joe")), EARLY).tickets().getFirst();

      assertThat(ticket.type()).isEqualTo(TicketType.STUDENT);
      assertThat(ticket.price()).isEqualByComparingTo("99");
    }

    @Test
    void shouldStopEarlyBirdAfter300ValidTickets() {
      IntStream.range(0, 299).forEach(index -> existing.add(Ticket.sold(TicketType.EARLY_BIRD, holder("early" + index), new BigDecimal("249"))));
      var refunded = Ticket.sold(TicketType.EARLY_BIRD, holder("refunded"), new BigDecimal("249"));
      refunded.refund(EARLY);
      existing.add(refunded);

      var order = office.order(order(attendee("jane"), attendee("john")), EARLY);

      assertThat(order.tickets()).extracting(Ticket::type).containsExactly(TicketType.EARLY_BIRD, TicketType.STANDARD);
    }

    @Test
    void shouldDiscount10PercentFrom5Tickets() {
      var order = office.order(order(attendee("a"), attendee("b"), attendee("c"), attendee("d"), student("e")), LATE);

      assertThat(order.tickets()).extracting(Ticket::price).map(BigDecimal::toPlainString).containsExactly("314.10", "314.10", "314.10", "314.10", "89.10");
      assertThat(order.total()).isEqualByComparingTo("1345.50");
    }

    @Test
    void shouldNotDiscountUnder5Tickets() {
      var order = office.order(order(attendee("a"), attendee("b"), attendee("c"), attendee("d")), LATE);

      assertThat(order.total()).isEqualByComparingTo("1396");
    }

    @Test
    void shouldNotSellMoreThan1200Tickets() {
      IntStream.range(0, 1199).forEach(index -> existing.add(Ticket.free(TicketType.SPONSOR, holder("guest" + index))));
      var refunded = standard();
      refunded.refund(EARLY);
      existing.add(refunded);

      assertThat(office.order(order(attendee("last")), LATE).tickets()).hasSize(1);
      existing.add(standard());

      assertThatThrownBy(() -> office.order(order(attendee("late")), LATE)).isExactlyInstanceOf(SoldOutException.class);
    }

    @Test
    void shouldNotOrderWithoutAttendee() {
      assertThatThrownBy(() -> new OrderToPlace(new Email("buyer@company.com"), List.of())).isInstanceOf(AssertionException.class);
    }

    private OrderToPlace order(AttendeeToRegister... attendees) {
      return new OrderToPlace(new Email("buyer@company.com"), List.of(attendees));
    }
  }

  @Nested
  class FreeTickets {

    @Test
    void shouldIssueSpeakerTicket() {
      var ticket = office.issueSpeakerTicket(jane());

      assertThat(ticket.type()).isEqualTo(TicketType.SPEAKER);
      verify(tickets).save(ticket);
    }

    @Test
    void shouldIssueSpeakerTicketOnlyOnce() {
      var speaker = Ticket.free(TicketType.SPEAKER, jane());
      existing.add(speaker);

      assertThat(office.issueSpeakerTicket(jane())).isSameAs(speaker);
      verify(tickets, never()).save(any());
    }

    @Test
    void shouldRefundTicketBoughtBySpeaker() {
      var bought = standard(jane());
      var boughtForSomeoneElse = standard(holder("john"));
      existing.addAll(List.of(bought, boughtForSomeoneElse));

      office.issueSpeakerTicket(jane());

      assertThat(bought.status()).isEqualTo(TicketStatus.REFUNDED);
      assertThat(boughtForSomeoneElse.isValid()).isTrue();
      verify(tickets).save(bought);
    }

    @Test
    void shouldIssueNewSpeakerTicketAfterRevocation() {
      var revoked = Ticket.free(TicketType.SPEAKER, jane());
      revoked.cancel();
      existing.add(revoked);

      assertThat(office.issueSpeakerTicket(jane())).isNotSameAs(revoked);
    }

    @Test
    void shouldRevokeSpeakerTicket() {
      var speaker = Ticket.free(TicketType.SPEAKER, jane());
      var bought = standard(jane());
      existing.addAll(List.of(speaker, bought, Ticket.free(TicketType.SPEAKER, holder("john"))));

      office.revokeSpeakerTicket(jane().email());

      assertThat(speaker.status()).isEqualTo(TicketStatus.CANCELLED);
      assertThat(bought.isValid()).isTrue();
      verify(tickets).save(speaker);
    }

    @Test
    void shouldIssueSponsorTickets() {
      var issued = office.issueSponsorTickets(jane(), 5);

      assertThat(issued).hasSize(5).allSatisfy(ticket -> assertThat(ticket.type()).isEqualTo(TicketType.SPONSOR));
      verify(tickets, times(5)).save(any());
    }
  }

  @Nested
  class Changes {

    @Test
    void shouldRenameTicket() {
      var ticket = known(standard());

      office.rename(ticket.number(), holder("john"), EARLY);

      assertThat(ticket.holder()).isEqualTo(holder("john"));
      verify(tickets).save(ticket);
    }

    @Test
    void shouldRefundTicket() {
      var ticket = known(standard());

      office.refund(ticket.number(), EARLY);

      assertThat(ticket.status()).isEqualTo(TicketStatus.REFUNDED);
      verify(tickets).save(ticket);
    }

    @Test
    void shouldFindTicket() {
      var ticket = known(standard());
      var unknown = TicketNumber.generate();
      doReturn(Optional.empty()).when(tickets).get(unknown);

      assertThat(office.findTicket(ticket.number())).contains(ticket);
      assertThat(office.findTicket(unknown)).isEmpty();
    }

    @Test
    void shouldNotFindUnknownTicket() {
      var unknown = TicketNumber.generate();
      doReturn(Optional.empty()).when(tickets).get(unknown);

      assertThatThrownBy(() -> office.ticket(unknown)).isExactlyInstanceOf(UnknownTicketException.class);
    }
  }

  @Nested
  class WorkshopRegistrations {

    private final UUID workshop = workshop();

    @BeforeEach
    void loadWorkshop() {
      doReturn(true).when(workshops).exists(workshop);
      doReturn(Optional.empty()).when(rosters).get(workshop);
    }

    @Test
    void shouldRegisterValidTicket() {
      var ticket = known(standard());

      var registration = office.register(workshop, ticket.number(), NOW);

      assertThat(registration.status()).isEqualTo(RegistrationStatus.REGISTERED);
      verify(rosters).save(argThat(roster -> roster.workshop().equals(workshop)));
    }

    @Test
    void shouldNotRegisterInvalidTicket() {
      var ticket = known(standard());
      ticket.refund(EARLY);

      assertThatThrownBy(() -> office.register(workshop, ticket.number(), NOW)).isExactlyInstanceOf(TicketNotValidException.class);
    }

    @Test
    void shouldNotRegisterToUnknownWorkshop() {
      var ticket = known(standard());
      var unknown = workshop();

      assertThatThrownBy(() -> office.register(unknown, ticket.number(), NOW)).isExactlyInstanceOf(UnknownWorkshopException.class);
    }

    @Test
    void shouldCancelRegistrationOfKnownRoster() {
      var ticket = known(standard());
      var roster = new WorkshopRoster(workshop);
      roster.register(ticket.number(), NOW);
      doReturn(Optional.of(roster)).when(rosters).get(workshop);

      office.cancelRegistration(workshop, ticket.number());

      assertThat(office.isRegistered(workshop, ticket.number())).isFalse();
      assertThat(office.roster(workshop)).hasSize(1);
      verify(rosters).save(roster);
    }

    @Test
    void shouldHaveEmptyRosterForWorkshopWithoutRegistration() {
      assertThat(office.roster(workshop)).isEmpty();
      assertThat(office.isRegistered(workshop, TicketNumber.generate())).isFalse();
      assertThatThrownBy(() -> office.cancelRegistration(workshop, TicketNumber.generate())).isExactlyInstanceOf(NotRegisteredException.class);
    }
  }

  private Ticket known(Ticket ticket) {
    doReturn(Optional.of(ticket)).when(tickets).get(ticket.number());

    return ticket;
  }
}
