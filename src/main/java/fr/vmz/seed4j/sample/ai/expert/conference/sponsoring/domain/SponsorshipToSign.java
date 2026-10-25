package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;

public record SponsorshipToSign(String company, String contactName, Email contactEmail, Pack pack) {
  public SponsorshipToSign {
    Assert.field("company", company).notBlank().maxLength(100);
    Assert.field("contactName", contactName).notBlank().maxLength(100);
    Assert.notNull("contactEmail", contactEmail);
    Assert.notNull("pack", pack);
  }

  public Sponsorship sign(LocalDate today) {
    return new Sponsorship(this, today);
  }
}
