package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record ProductName(String name) {
  public ProductName {
    Assert.notBlank("name", name);

    name = name.trim();
    Assert.field("name", name).maxLength(100);
  }

  public boolean sameAs(ProductName other) {
    return name.equalsIgnoreCase(other.name());
  }

  public String get() {
    return name();
  }
}
