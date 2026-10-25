package fr.vmz.seed4j.sample.ai.advanced.shop.domain.article;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record ArticleName(String name) {
  public ArticleName {
    Assert.notBlank("name", name);

    name = name.trim();
    Assert.field("name", name).maxLength(100);
  }

  public boolean sameAs(ArticleName other) {
    return name.equalsIgnoreCase(other.name());
  }

  public String get() {
    return name();
  }
}
