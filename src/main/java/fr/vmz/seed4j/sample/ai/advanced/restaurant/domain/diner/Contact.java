package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record Contact(String fullName, DinerEmail email, PhoneNumber phone) {
  public Contact {
    Assert.field("fullName", fullName).notBlank().maxLength(100);
    Assert.notNull("email", email);
    Assert.notNull("phone", phone);

    fullName = fullName.trim();
  }
}
