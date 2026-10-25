package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain;

import static fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain.SponsorshipsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import java.util.List;
import org.junit.jupiter.api.Test;

@UnitTest
class SponsorshipTest {

  @Test
  void shouldDescribePacks() {
    assertThat(Pack.GOLD.price()).isEqualByComparingTo("8000");
    assertThat(Pack.GOLD.tickets()).isEqualTo(5);
    assertThat(Pack.SILVER.tickets()).isEqualTo(2);
    assertThat(Pack.BRONZE.tickets()).isEqualTo(1);
    assertThat(Pack.GOLD.isAvailableWith(2)).isTrue();
    assertThat(Pack.GOLD.isAvailableWith(3)).isFalse();
    assertThat(Pack.SILVER.isAvailableWith(8)).isFalse();
    assertThat(Pack.BRONZE.isAvailableWith(1000)).isTrue();
  }

  @Test
  void shouldReservePackOnSignature() {
    var sponsorship = reserved(Pack.SILVER);

    assertThat(sponsorship.id()).isNotNull();
    assertThat(sponsorship.company()).isEqualTo("Acme");
    assertThat(sponsorship.contactName()).isEqualTo("Wile Coyote");
    assertThat(sponsorship.contactEmail()).isEqualTo(new Email("wile@acme.com"));
    assertThat(sponsorship.pack()).isEqualTo(Pack.SILVER);
    assertThat(sponsorship.signedOn()).isEqualTo(SIGNED_ON);
    assertThat(sponsorship.paymentDeadline()).isEqualTo(SIGNED_ON.plusDays(30));
    assertThat(sponsorship.status()).isEqualTo(SponsorshipStatus.RESERVED);
    assertThat(sponsorship.holdsPack()).isTrue();
    assertThat(sponsorship.tickets()).isEmpty();
  }

  @Test
  void shouldConfirmPackOnPaymentWithin30Days() {
    var sponsorship = reserved(Pack.SILVER);

    sponsorship.confirmPayment(SIGNED_ON.plusDays(30));

    assertThat(sponsorship.status()).isEqualTo(SponsorshipStatus.CONFIRMED);
    assertThat(sponsorship.holdsPack()).isTrue();
  }

  @Test
  void shouldNotConfirmPackAfter30Days() {
    var sponsorship = reserved(Pack.SILVER);

    assertThatThrownBy(() -> sponsorship.confirmPayment(SIGNED_ON.plusDays(31))).isExactlyInstanceOf(PaymentDeadlinePassedException.class);
  }

  @Test
  void shouldNotConfirmPackTwice() {
    var sponsorship = confirmed(Pack.SILVER);

    assertThatThrownBy(() -> sponsorship.confirmPayment(SIGNED_ON))
      .isExactlyInstanceOf(InvalidSponsorshipTransitionException.class)
      .hasMessageContaining("CONFIRMED");
  }

  @Test
  void shouldReleaseUnpaidPackAfter30Days() {
    var sponsorship = reserved(Pack.GOLD);

    assertThat(sponsorship.releaseIfUnpaid(SIGNED_ON.plusDays(30))).isFalse();
    assertThat(sponsorship.releaseIfUnpaid(SIGNED_ON.plusDays(31))).isTrue();

    assertThat(sponsorship.status()).isEqualTo(SponsorshipStatus.RELEASED);
    assertThat(sponsorship.holdsPack()).isFalse();
  }

  @Test
  void shouldNotReleasePaidPack() {
    assertThat(confirmed(Pack.GOLD).releaseIfUnpaid(SIGNED_ON.plusYears(1))).isFalse();
  }

  @Test
  void shouldGiveOneSponsoredTalkToConfirmedGold() {
    var gold = confirmed(Pack.GOLD);

    assertThat(gold.claimTalk()).isTrue();
    assertThat(gold.claimTalk()).isFalse();
  }

  @Test
  void shouldNotGiveSponsoredTalkToOtherPacksOrUnpaidGold() {
    assertThat(confirmed(Pack.SILVER).claimTalk()).isFalse();
    assertThat(reserved(Pack.GOLD).claimTalk()).isFalse();
  }

  @Test
  void shouldKeepIssuedTickets() {
    var sponsorship = confirmed(Pack.BRONZE);

    sponsorship.attachTickets(List.of("RC-0001"));

    assertThat(sponsorship.tickets()).containsExactly("RC-0001");
  }

  @Test
  void shouldNotSignWithoutCompany() {
    assertThatThrownBy(() -> new SponsorshipToSign(" ", "Wile", new Email("wile@acme.com"), Pack.GOLD)).isInstanceOf(AssertionException.class);
  }
}
