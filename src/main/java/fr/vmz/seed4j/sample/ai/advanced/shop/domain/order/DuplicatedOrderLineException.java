package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.InvalidShopValueException;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticleId;

class DuplicatedOrderLineException extends InvalidShopValueException {

  DuplicatedOrderLineException(ArticleId article) {
    super("Article " + article.get() + " appears in several lines of the order");
  }
}
