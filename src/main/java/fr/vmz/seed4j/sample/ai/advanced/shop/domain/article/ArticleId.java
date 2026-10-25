package fr.vmz.seed4j.sample.ai.advanced.shop.domain.article;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.UUID;

public record ArticleId(UUID id) {
  public ArticleId {
    Assert.notNull("id", id);
  }

  public static ArticleId newId() {
    return new ArticleId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
