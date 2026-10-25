package fr.vmz.seed4j.sample.ai.advanced.shop.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticleId;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.LineToOrder;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.Quantity;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestLineToOrder(@Nullable UUID articleId, int quantity) {
  LineToOrder toDomain() {
    return new LineToOrder(new ArticleId(articleId), new Quantity(quantity));
  }
}
