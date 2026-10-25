package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

import static fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.TicketsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

@UnitTest
class WorkshopRosterTest {

  private final WorkshopRoster roster = new WorkshopRoster(workshop());

  @Test
  void shouldRegisterUpTo30Tickets() {
    var registrations = IntStream.range(0, 30).mapToObj(index -> roster.register(TicketNumber.generate(), NOW.plusMinutes(index))).toList();

    assertThat(registrations).allSatisfy(registration -> assertThat(registration.status()).isEqualTo(RegistrationStatus.REGISTERED));
    assertThat(roster.register(TicketNumber.generate(), NOW.plusHours(1)).status()).isEqualTo(RegistrationStatus.WAITING);
  }

  @Test
  void shouldNotRegisterTicketTwice() {
    var ticket = TicketNumber.generate();
    roster.register(ticket, NOW);

    assertThatThrownBy(() -> roster.register(ticket, NOW)).isExactlyInstanceOf(AlreadyRegisteredException.class);
  }

  @Test
  void shouldGiveFreedSeatToFirstWaitingTicket() {
    var registered = fill();
    var first = TicketNumber.generate();
    var second = TicketNumber.generate();
    roster.register(second, NOW.plusHours(2));
    roster.register(first, NOW.plusHours(1));

    var promoted = roster.cancel(registered);

    assertThat(promoted).hasValueSatisfying(registration -> assertThat(registration.ticket()).isEqualTo(first));
    assertThat(roster.isRegistered(first)).isTrue();
    assertThat(roster.isRegistered(second)).isFalse();
    assertThat(roster.isRegistered(registered)).isFalse();
  }

  @Test
  void shouldNotPromoteWhenWaitingTicketLeaves() {
    fill();
    var waiting = TicketNumber.generate();
    roster.register(waiting, NOW.plusHours(1));
    var next = TicketNumber.generate();
    roster.register(next, NOW.plusHours(2));

    assertThat(roster.cancel(waiting)).isEmpty();
    assertThat(roster.isRegistered(next)).isFalse();
  }

  @Test
  void shouldNotPromoteWithoutWaitingTicket() {
    var ticket = TicketNumber.generate();
    roster.register(ticket, NOW);

    assertThat(roster.cancel(ticket)).isEmpty();
  }

  @Test
  void shouldRegisterAgainAfterCancellation() {
    var ticket = TicketNumber.generate();
    roster.register(ticket, NOW);
    roster.cancel(ticket);

    assertThat(roster.register(ticket, NOW.plusMinutes(1)).status()).isEqualTo(RegistrationStatus.REGISTERED);
    assertThat(roster.registrations()).hasSize(2);
  }

  @Test
  void shouldNotCancelUnknownRegistration() {
    assertThatThrownBy(() -> roster.cancel(TicketNumber.generate())).isExactlyInstanceOf(NotRegisteredException.class);
  }

  private TicketNumber fill() {
    var first = TicketNumber.generate();
    roster.register(first, NOW);
    IntStream.range(1, 30).forEach(index -> roster.register(TicketNumber.generate(), NOW.plusMinutes(index)));

    return first;
  }
}
