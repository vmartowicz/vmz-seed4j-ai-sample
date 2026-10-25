package fr.vmz.seed4j.sample.ai.advanced.shop.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.Article;
import java.math.BigDecimal;
import java.util.UUID;

record RestArticle(UUID id, String name, BigDecimal unitPrice, int stock, boolean lowStock) {
  static RestArticle from(Article article) {
    return new RestArticle(article.id().get(), article.name().get(), article.price().get(), article.stock().get(), article.stock().isLow());
  }
}
