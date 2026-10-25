package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class Sponsorship {

  private static final int PAYMENT_DAYS = 30;

  private final SponsorId id;
  private final String company;
  private final String contactName;
  private final Email contactEmail;
  private final Pack pack;
  private final LocalDate signedOn;
  private final List<String> tickets = new ArrayList<>();
  private SponsorshipStatus status;
  private boolean talkClaimed;

  Sponsorship(SponsorshipToSign sponsorship, LocalDate signedOn) {
    id = SponsorId.newId();
    company = sponsorship.company().trim();
    contactName = sponsorship.contactName().trim();
    contactEmail = sponsorship.contactEmail();
    pack = sponsorship.pack();
    this.signedOn = signedOn;
    status = SponsorshipStatus.RESERVED;
  }

  public void confirmPayment(LocalDate today) {
    if (status != SponsorshipStatus.RESERVED) {
      throw new InvalidSponsorshipTransitionException(id, status);
    }

    if (today.isAfter(paymentDeadline())) {
      throw new PaymentDeadlinePassedException(id, paymentDeadline());
    }

    status = SponsorshipStatus.CONFIRMED;
  }

  public boolean releaseIfUnpaid(LocalDate today) {
    if (status != SponsorshipStatus.RESERVED || !today.isAfter(paymentDeadline())) {
      return false;
    }

    status = SponsorshipStatus.RELEASED;
    return true;
  }

  public boolean claimTalk() {
    if (pack != Pack.GOLD || status != SponsorshipStatus.CONFIRMED || talkClaimed) {
      return false;
    }

    talkClaimed = true;
    return true;
  }

  public void attachTickets(List<String> issued) {
    tickets.addAll(issued);
  }

  public boolean holdsPack() {
    return status != SponsorshipStatus.RELEASED;
  }

  public LocalDate paymentDeadline() {
    return signedOn.plusDays(PAYMENT_DAYS);
  }

  public SponsorId id() {
    return id;
  }

  public String company() {
    return company;
  }

  public String contactName() {
    return contactName;
  }

  public Email contactEmail() {
    return contactEmail;
  }

  public Pack pack() {
    return pack;
  }

  public LocalDate signedOn() {
    return signedOn;
  }

  public SponsorshipStatus status() {
    return status;
  }

  public List<String> tickets() {
    return List.copyOf(tickets);
  }
}
