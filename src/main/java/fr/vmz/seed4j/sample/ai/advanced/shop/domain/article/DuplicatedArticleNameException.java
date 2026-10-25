package fr.vmz.seed4j.sample.ai.advanced.shop.domain.article;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.ShopRuleViolationException;

class DuplicatedArticleNameException extends ShopRuleViolationException {

  DuplicatedArticleNameException(ArticleName name) {
    super("An article is already named " + name.get());
  }
}
