package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import java.time.LocalDate;

public final class SponsorshipsFixture {

  public static final LocalDate SIGNED_ON = LocalDate.of(2026, 5, 4);

  private SponsorshipsFixture() {}

  public static SponsorshipToSign signing(Pack pack) {
    return new SponsorshipToSign("Acme", "Wile Coyote", new Email("wile@acme.com"), pack);
  }

  public static Sponsorship reserved(Pack pack) {
    return signing(pack).sign(SIGNED_ON);
  }

  public static Sponsorship confirmed(Pack pack) {
    var sponsorship = reserved(pack);
    sponsorship.confirmPayment(SIGNED_ON);

    return sponsorship;
  }
}
