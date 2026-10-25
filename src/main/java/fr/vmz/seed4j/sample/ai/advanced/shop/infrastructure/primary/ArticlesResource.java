package fr.vmz.seed4j.sample.ai.advanced.shop.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.shop.application.ArticlesApplicationService;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.Article;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticleId;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shop/articles")
class ArticlesResource {

  private final ArticlesApplicationService articles;

  ArticlesResource(ArticlesApplicationService articles) {
    this.articles = articles;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  RestArticle add(@RequestBody RestArticleToAdd article) {
    return RestArticle.from(articles.add(article.toDomain()));
  }

  @GetMapping
  List<RestArticle> list() {
    return toRest(articles.all());
  }

  @PostMapping("/{articleId}/restock")
  RestArticle restock(@PathVariable UUID articleId, @RequestBody RestRestock restock) {
    return RestArticle.from(articles.restock(new ArticleId(articleId), restock.toDomain()));
  }

  @GetMapping("/low-stock")
  List<RestArticle> lowStock() {
    return toRest(articles.lowStock());
  }

  private static List<RestArticle> toRest(List<Article> articles) {
    return articles.stream().map(RestArticle::from).toList();
  }
}
