package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain.Pack;
import fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain.SponsorshipToSign;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import org.jspecify.annotations.Nullable;

record RestSponsorshipToSign(@Nullable String company, @Nullable String contactName, @Nullable String contactEmail, @Nullable Pack pack) {
  SponsorshipToSign toDomain() {
    return new SponsorshipToSign(company, contactName, new Email(contactEmail), pack);
  }
}
