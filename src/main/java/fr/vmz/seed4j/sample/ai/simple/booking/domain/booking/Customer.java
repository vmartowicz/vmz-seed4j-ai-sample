package fr.vmz.seed4j.sample.ai.simple.booking.domain.booking;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record Customer(CustomerName name, MobilePhone phone) {
  public Customer {
    Assert.notNull("name", name);
    Assert.notNull("phone", phone);
  }
}
