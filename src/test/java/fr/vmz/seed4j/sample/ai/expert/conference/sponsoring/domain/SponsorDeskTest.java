package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain;

import static fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain.SponsorshipsFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@UnitTest
class SponsorDeskTest {

  private final SponsorshipsRepository sponsorships = mock(SponsorshipsRepository.class, CALLS_REAL_METHODS);
  private final SponsorTickets tickets = mock(SponsorTickets.class);
  private final SponsorDesk desk = new SponsorDesk(sponsorships, tickets);

  @BeforeEach
  void loadSponsorships() {
    doReturn(List.of()).when(sponsorships).all();
  }

  @Test
  void shouldReservePack() {
    var sponsorship = desk.sign(signing(Pack.GOLD), SIGNED_ON);

    assertThat(sponsorship.status()).isEqualTo(SponsorshipStatus.RESERVED);
    verify(sponsorships).save(sponsorship);
  }

  @Test
  void shouldNotSellMoreThan3Gold() {
    var released = reserved(Pack.GOLD);
    released.releaseIfUnpaid(SIGNED_ON.plusDays(31));
    doReturn(List.of(reserved(Pack.GOLD), confirmed(Pack.GOLD), released, reserved(Pack.SILVER))).when(sponsorships).all();
    assertThat(desk.sign(signing(Pack.GOLD), SIGNED_ON).pack()).isEqualTo(Pack.GOLD);

    doReturn(List.of(reserved(Pack.GOLD), confirmed(Pack.GOLD), reserved(Pack.GOLD))).when(sponsorships).all();

    assertThatThrownBy(() -> desk.sign(signing(Pack.GOLD), SIGNED_ON)).isExactlyInstanceOf(PackSoldOutException.class).hasMessageContaining("GOLD");
  }

  @Test
  void shouldIssuePackTicketsOnPayment() {
    var sponsorship = known(reserved(Pack.GOLD));
    doReturn(List.of("RC-1", "RC-2", "RC-3", "RC-4", "RC-5")).when(tickets).issue("Wile Coyote", new Email("wile@acme.com"), 5);

    desk.pay(sponsorship.id(), SIGNED_ON);

    assertThat(sponsorship.status()).isEqualTo(SponsorshipStatus.CONFIRMED);
    assertThat(sponsorship.tickets()).hasSize(5);
    verify(sponsorships).save(sponsorship);
  }

  @Test
  void shouldNotIssueTicketsForLatePayment() {
    var sponsorship = known(reserved(Pack.GOLD));

    assertThatThrownBy(() -> desk.pay(sponsorship.id(), SIGNED_ON.plusDays(31))).isExactlyInstanceOf(PaymentDeadlinePassedException.class);
    verifyNoInteractions(tickets);
  }

  @Test
  void shouldReleaseUnpaidPacks() {
    var unpaid = reserved(Pack.GOLD);
    var paid = confirmed(Pack.GOLD);
    doReturn(List.of(unpaid, paid)).when(sponsorships).all();

    desk.releaseUnpaid(SIGNED_ON.plusDays(31));

    assertThat(unpaid.status()).isEqualTo(SponsorshipStatus.RELEASED);
    verify(sponsorships).save(unpaid);
    verify(sponsorships, never()).save(paid);
  }

  @Test
  void shouldClaimSponsoredTalkOnce() {
    var gold = known(confirmed(Pack.GOLD));

    assertThat(desk.claimTalk(gold.id())).isTrue();
    assertThat(desk.claimTalk(gold.id())).isFalse();
    verify(sponsorships, times(1)).save(gold);
  }

  @Test
  void shouldNotClaimTalkOfUnknownSponsor() {
    var unknown = SponsorId.newId();
    doReturn(Optional.empty()).when(sponsorships).get(unknown);

    assertThat(desk.claimTalk(unknown)).isFalse();
  }

  @Test
  void shouldNotPayUnknownSponsorship() {
    var unknown = SponsorId.newId();
    doReturn(Optional.empty()).when(sponsorships).get(unknown);

    assertThatThrownBy(() -> desk.pay(unknown, SIGNED_ON)).isExactlyInstanceOf(UnknownSponsorshipException.class);
    verify(sponsorships, never()).save(any());
  }

  private Sponsorship known(Sponsorship sponsorship) {
    doReturn(Optional.of(sponsorship)).when(sponsorships).get(sponsorship.id());

    return sponsorship;
  }
}
