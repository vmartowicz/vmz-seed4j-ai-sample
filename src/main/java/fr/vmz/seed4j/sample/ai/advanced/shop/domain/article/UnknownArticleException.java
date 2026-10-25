package fr.vmz.seed4j.sample.ai.advanced.shop.domain.article;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.UnknownShopElementException;

public class UnknownArticleException extends UnknownShopElementException {

  UnknownArticleException(ArticleId article) {
    super("Unknown article " + article.get());
  }
}
