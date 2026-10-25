package fr.vmz.seed4j.sample.ai.advanced.shop.domain.article;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.ShopRuleViolationException;

public class InsufficientStockException extends ShopRuleViolationException {

  InsufficientStockException(ArticleName article, int quantity) {
    super("Not enough " + article.get() + " in stock for " + quantity + " units");
  }
}
