package fr.vmz.seed4j.sample.ai.advanced.shop.infrastructure.primary;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@AutoConfigureMockMvc
@Import(MovableClockConfiguration.class)
class ArticlesResourceIT {

  // In-memory articles are shared by the whole Spring context: each test adds its own articles
  @Autowired
  private MockMvc rest;

  private ShopApi api;

  @BeforeEach
  void loadApi() {
    api = new ShopApi(rest);
  }

  @Test
  void shouldAddArticle() throws Exception {
    var name = ShopApi.uniqueName();

    api
      .addArticle(name, "20", 10)
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").isNotEmpty())
      .andExpect(jsonPath("$.name").value(name))
      .andExpect(jsonPath("$.unitPrice").value(20.0))
      .andExpect(jsonPath("$.stock").value(10))
      .andExpect(jsonPath("$.lowStock").value(false));
  }

  @Test
  void shouldNotAddTwoArticlesWithTheSameName() throws Exception {
    var name = ShopApi.uniqueName();
    api.addArticle(name, "20", 10).andExpect(status().isCreated());

    api.addArticle(name.toUpperCase(), "20", 10).andExpect(status().isConflict()).andExpect(jsonPath("$.title").value("Business rule violation"));
  }

  @Test
  void shouldNotAddArticleWithInvalidPrice() throws Exception {
    api.addArticle(ShopApi.uniqueName(), "9.999", 10).andExpect(status().isBadRequest()).andExpect(jsonPath("$.title").value("Invalid value"));
  }

  @Test
  void shouldListArticles() throws Exception {
    var articleId = api.articleId("20", 10);

    rest.perform(get("/api/shop/articles")).andExpect(status().isOk()).andExpect(jsonPath("$[*].id", hasItem(articleId)));
  }

  @Test
  void shouldRestockArticle() throws Exception {
    var articleId = api.articleId("20", 2);

    api
      .postJson("/api/shop/articles/" + articleId + "/restock", "{\"quantity\":10}")
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.stock").value(12));
  }

  @Test
  void shouldNotRestockNothing() throws Exception {
    api.postJson("/api/shop/articles/" + api.articleId("20", 2) + "/restock", "{\"quantity\":0}").andExpect(status().isBadRequest());
  }

  @Test
  void shouldNotRestockUnknownArticle() throws Exception {
    api
      .postJson("/api/shop/articles/" + UUID.randomUUID() + "/restock", "{\"quantity\":1}")
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.title").value("Unknown element"));
  }

  @Test
  void shouldListArticlesInStockAlert() throws Exception {
    var low = api.articleId("20", 4);
    var enough = api.articleId("20", 5);

    rest
      .perform(get("/api/shop/articles/low-stock"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[*].id", hasItem(low)))
      .andExpect(jsonPath("$[*].id", not(hasItem(enough))));
  }
}
