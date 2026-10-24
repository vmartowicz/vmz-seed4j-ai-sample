package fr.vmz.seed4j.sample.ai.clinic.domain.owner;

import fr.vmz.seed4j.sample.ai.clinic.domain.FirstName;
import fr.vmz.seed4j.sample.ai.clinic.domain.LastName;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record OwnerInformation(FirstName firstName, LastName lastName, Address address, City city, Telephone telephone) {
  public OwnerInformation {
    Assert.notNull("firstName", firstName);
    Assert.notNull("lastName", lastName);
    Assert.notNull("address", address);
    Assert.notNull("city", city);
    Assert.notNull("telephone", telephone);
  }
}
