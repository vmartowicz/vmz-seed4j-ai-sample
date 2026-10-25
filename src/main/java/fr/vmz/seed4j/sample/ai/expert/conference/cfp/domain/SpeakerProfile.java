package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record SpeakerProfile(Email email, String name, String company, boolean fromAbroad, boolean spokeBefore) {
  public SpeakerProfile {
    Assert.notNull("email", email);
    Assert.field("name", name).notBlank().maxLength(100);
    Assert.field("company", company).notBlank().maxLength(100);
  }
}
