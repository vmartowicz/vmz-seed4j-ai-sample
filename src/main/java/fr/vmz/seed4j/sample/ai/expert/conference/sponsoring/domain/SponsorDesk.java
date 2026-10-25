package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;

public class SponsorDesk {

  private final SponsorshipsRepository sponsorships;
  private final SponsorTickets tickets;

  public SponsorDesk(SponsorshipsRepository sponsorships, SponsorTickets tickets) {
    this.sponsorships = sponsorships;
    this.tickets = tickets;
  }

  public Sponsorship sign(SponsorshipToSign toSign, LocalDate today) {
    Assert.notNull("sponsorship", toSign);

    var holders = sponsorships
      .all()
      .stream()
      .filter(sponsorship -> sponsorship.pack() == toSign.pack() && sponsorship.holdsPack())
      .count();
    if (!toSign.pack().isAvailableWith(holders)) {
      throw new PackSoldOutException(toSign.pack());
    }

    var sponsorship = toSign.sign(today);
    sponsorships.save(sponsorship);

    return sponsorship;
  }

  public Sponsorship pay(SponsorId id, LocalDate today) {
    var sponsorship = sponsorships.sponsorship(id);

    sponsorship.confirmPayment(today);
    sponsorship.attachTickets(tickets.issue(sponsorship.contactName(), sponsorship.contactEmail(), sponsorship.pack().tickets()));
    sponsorships.save(sponsorship);

    return sponsorship;
  }

  public void releaseUnpaid(LocalDate today) {
    sponsorships
      .all()
      .stream()
      .filter(sponsorship -> sponsorship.releaseIfUnpaid(today))
      .forEach(sponsorships::save);
  }

  public boolean claimTalk(SponsorId id) {
    return sponsorships
      .get(id)
      .filter(Sponsorship::claimTalk)
      .map(sponsorship -> {
        sponsorships.save(sponsorship);
        return true;
      })
      .orElse(false);
  }

  public Sponsorship sponsorship(SponsorId id) {
    return sponsorships.sponsorship(id);
  }
}
