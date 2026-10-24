package fr.vmz.seed4j.sample.ai.clinic.domain.owner;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record PetName(String name) {
  public PetName {
    Assert.notBlank("name", name);

    name = name.trim();
    Assert.field("name", name).maxLength(30);
  }

  public boolean sameAs(PetName other) {
    return name.equalsIgnoreCase(other.name());
  }

  public String get() {
    return name();
  }
}
