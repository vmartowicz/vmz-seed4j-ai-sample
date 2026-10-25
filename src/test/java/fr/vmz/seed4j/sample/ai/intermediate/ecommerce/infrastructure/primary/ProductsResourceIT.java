package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.infrastructure.primary;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@IntegrationTest
@AutoConfigureMockMvc
class ProductsResourceIT {

  // In-memory products are shared by the whole Spring context: each test uses its own product names
  @Autowired
  private MockMvc rest;

  @Test
  void shouldAddProduct() throws Exception {
    var name = uniqueName();

    addProduct(name, "19.99", 10)
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").isNotEmpty())
      .andExpect(jsonPath("$.name").value(name))
      .andExpect(jsonPath("$.unitPrice").value(19.99))
      .andExpect(jsonPath("$.stock").value(10));
  }

  @Test
  void shouldNotAddTwoProductsWithSameName() throws Exception {
    var name = uniqueName();
    addProduct(name, "19.99", 10).andExpect(status().isCreated());

    addProduct(name.toLowerCase(), "9.99", 1)
      .andExpect(status().isConflict())
      .andExpect(jsonPath("$.title").value("Business rule violation"));
  }

  @Test
  void shouldNotAddProductWithInvalidPrice() throws Exception {
    addProduct(uniqueName(), "9.999", 10)
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.title").value("Invalid value"));
  }

  @Test
  void shouldNotAddProductWithNegativeStock() throws Exception {
    addProduct(uniqueName(), "9.99", -1).andExpect(status().isBadRequest());
  }

  @Test
  void shouldListProducts() throws Exception {
    var name = uniqueName();
    addProduct(name, "19.99", 10);

    rest.perform(get("/api/ecommerce/products")).andExpect(status().isOk()).andExpect(jsonPath("$[*].name", hasItem(name)));
  }

  private ResultActions addProduct(String name, String price, int stock) throws Exception {
    var body = "{\"name\":\"%s\",\"unitPrice\":%s,\"stock\":%d}".formatted(name, price, stock);

    return rest.perform(post("/api/ecommerce/products").contentType(MediaType.APPLICATION_JSON).content(body));
  }

  private static String uniqueName() {
    return "Product " + UUID.randomUUID().toString().substring(0, 8);
  }
}
