package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record Holder(String name, Email email) {
  public Holder {
    Assert.field("name", name).notBlank().maxLength(100);
    Assert.notNull("email", email);

    name = name.trim();
  }
}
