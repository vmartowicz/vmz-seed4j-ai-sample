package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

import static fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.TicketsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

@UnitTest
class TicketTest {

  @Test
  void shouldSellTicket() {
    var ticket = standard();

    assertThat(ticket.number().get()).matches("RC-[0-9A-F]{8}");
    assertThat(ticket.type()).isEqualTo(TicketType.STANDARD);
    assertThat(ticket.holder()).isEqualTo(jane());
    assertThat(ticket.price()).isEqualByComparingTo("349");
    assertThat(ticket.status()).isEqualTo(TicketStatus.VALID);
    assertThat(ticket.isValid()).isTrue();
  }

  @Test
  void shouldIssueFreeTicket() {
    var ticket = Ticket.free(TicketType.SPEAKER, jane());

    assertThat(ticket.price()).isEqualByComparingTo("0");
    assertThat(ticket.type().isSold()).isFalse();
  }

  @Test
  void shouldNormalizeTicketNumber() {
    assertThat(new TicketNumber(" rc-0a1b2c3d ").get()).isEqualTo("RC-0A1B2C3D");
  }

  @Test
  void shouldRenameUntil7DaysBeforeConference() {
    var ticket = standard();

    ticket.rename(holder("john"), LocalDate.of(2026, 10, 8));

    assertThat(ticket.holder()).isEqualTo(holder("john"));
  }

  @Test
  void shouldNotRenameLessThan7DaysBeforeConference() {
    var ticket = standard();

    assertThatThrownBy(() -> ticket.rename(holder("john"), LocalDate.of(2026, 10, 9))).isExactlyInstanceOf(NameChangeClosedException.class);
  }

  @Test
  void shouldRefundUntil30DaysBeforeConference() {
    var ticket = standard();

    ticket.refund(LocalDate.of(2026, 9, 15));

    assertThat(ticket.status()).isEqualTo(TicketStatus.REFUNDED);
    assertThat(ticket.isValid()).isFalse();
  }

  @Test
  void shouldNotRefundLessThan30DaysBeforeConference() {
    var ticket = standard();

    assertThatThrownBy(() -> ticket.refund(LocalDate.of(2026, 9, 16))).isExactlyInstanceOf(RefundPeriodOverException.class);
  }

  @Test
  void shouldNotRefundFreeTicket() {
    var ticket = Ticket.free(TicketType.SPONSOR, jane());

    assertThatThrownBy(() -> ticket.refund(EARLY)).isExactlyInstanceOf(FreeTicketNotRefundableException.class);
  }

  @Test
  void shouldNotChangeInvalidTicket() {
    var ticket = standard();
    ticket.refund(EARLY);

    assertThatThrownBy(() -> ticket.refund(EARLY)).isExactlyInstanceOf(TicketNotValidException.class);
    assertThatThrownBy(() -> ticket.rename(holder("john"), EARLY)).isExactlyInstanceOf(TicketNotValidException.class);
  }

  @Test
  void shouldCancelFreeTicket() {
    var ticket = Ticket.free(TicketType.SPEAKER, jane());

    ticket.cancel();

    assertThat(ticket.status()).isEqualTo(TicketStatus.CANCELLED);
  }

  @Test
  void shouldRefundTicketReplacedBySpeakerTicket() {
    var ticket = standard();

    ticket.replaceBySpeakerTicket();

    assertThat(ticket.status()).isEqualTo(TicketStatus.REFUNDED);
  }

  @Test
  void shouldNotBuildHolderWithoutName() {
    assertThatThrownBy(() -> new Holder(" ", jane().email())).isInstanceOf(AssertionException.class);
  }
}
