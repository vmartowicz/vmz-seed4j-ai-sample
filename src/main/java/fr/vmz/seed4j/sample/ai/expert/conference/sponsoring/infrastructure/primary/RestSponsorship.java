package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain.Pack;
import fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain.Sponsorship;
import fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain.SponsorshipStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

record RestSponsorship(
  UUID id,
  String company,
  String contactName,
  String contactEmail,
  Pack pack,
  LocalDate signedOn,
  LocalDate paymentDeadline,
  SponsorshipStatus status,
  List<String> tickets
) {
  static RestSponsorship from(Sponsorship sponsorship) {
    return new RestSponsorship(
      sponsorship.id().get(),
      sponsorship.company(),
      sponsorship.contactName(),
      sponsorship.contactEmail().get(),
      sponsorship.pack(),
      sponsorship.signedOn(),
      sponsorship.paymentDeadline(),
      sponsorship.status(),
      sponsorship.tickets()
    );
  }
}
