package fr.vmz.seed4j.sample.ai.advanced.shop.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.Price;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticleName;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticleToAdd;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.Stock;
import java.math.BigDecimal;
import org.jspecify.annotations.Nullable;

record RestArticleToAdd(@Nullable String name, @Nullable BigDecimal unitPrice, int stock) {
  ArticleToAdd toDomain() {
    return new ArticleToAdd(new ArticleName(name), new Price(unitPrice), new Stock(stock));
  }
}
