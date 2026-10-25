package fr.vmz.seed4j.sample.ai.advanced.library.domain.book;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record Title(String title) {
  public Title {
    Assert.field("title", title).notBlank().maxLength(200);
  }

  public String get() {
    return title();
  }
}
