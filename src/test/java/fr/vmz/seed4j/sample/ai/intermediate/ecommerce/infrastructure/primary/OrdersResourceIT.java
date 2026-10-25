package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.infrastructure.primary;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;
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
class OrdersResourceIT {

  // In-memory data is shared by the whole Spring context: each test adds its own products
  @Autowired
  private MockMvc rest;

  @Test
  void shouldPlaceOrderAndViewItsTotal() throws Exception {
    var tShirt = addProduct("19.99", 10);
    var mug = addProduct("8.50", 5);
    var orderId = idOf(placeOrder("{\"productId\":\"" + tShirt + "\",\"quantity\":2},{\"productId\":\"" + mug + "\",\"quantity\":1}"));

    rest
      .perform(get("/api/ecommerce/orders/" + orderId))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.customerEmail").value("jane@shop.com"))
      .andExpect(jsonPath("$.status").value("PENDING"))
      .andExpect(jsonPath("$.lines[0].productId").value(tShirt))
      .andExpect(jsonPath("$.lines[0].quantity").value(2))
      .andExpect(jsonPath("$.lines[0].unitPrice").value(19.99))
      .andExpect(jsonPath("$.lines[0].total").value(39.98))
      .andExpect(jsonPath("$.total").value(48.48))
      .andExpect(jsonPath("$.deliveredOn").doesNotExist());
  }

  @Test
  void shouldReserveStockWhenPlacingOrder() throws Exception {
    var tShirt = addProduct("19.99", 3);
    placeOrder(line(tShirt, 2)).andExpect(status().isCreated());

    placeOrder(line(tShirt, 2))
      .andExpect(status().isConflict())
      .andExpect(jsonPath("$.title").value("Business rule violation"));
  }

  @Test
  void shouldNotPlaceOrderForUnknownProduct() throws Exception {
    placeOrder(line(UUID.randomUUID().toString(), 1))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.title").value("Unknown element"));
  }

  @Test
  void shouldNotPlaceOrderWithTwoLinesOfTheSameProduct() throws Exception {
    var tShirt = addProduct("19.99", 10);

    placeOrder(line(tShirt, 1) + "," + line(tShirt, 1))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.title").value("Invalid value"));
  }

  @Test
  void shouldNotPlaceOrderWithoutLines() throws Exception {
    rest
      .perform(post("/api/ecommerce/orders").contentType(MediaType.APPLICATION_JSON).content("{\"customerEmail\":\"jane@shop.com\"}"))
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldFollowOrderUntilReturn() throws Exception {
    var tShirt = addProduct("19.99", 2);
    var orderId = idOf(placeOrder(line(tShirt, 2)));

    move(orderId, "payment").andExpect(status().isOk()).andExpect(jsonPath("$.status").value("PAID"));
    move(orderId, "shipment").andExpect(jsonPath("$.status").value("SHIPPED"));
    move(orderId, "delivery").andExpect(jsonPath("$.status").value("DELIVERED")).andExpect(jsonPath("$.deliveredOn").isNotEmpty());
    move(orderId, "return").andExpect(status().isOk()).andExpect(jsonPath("$.status").value("RETURNED"));

    placeOrder(line(tShirt, 2)).andExpect(status().isCreated());
  }

  @Test
  void shouldReleaseStockWhenCancellingOrder() throws Exception {
    var tShirt = addProduct("19.99", 2);
    var orderId = idOf(placeOrder(line(tShirt, 2)));

    move(orderId, "cancellation").andExpect(status().isOk()).andExpect(jsonPath("$.status").value("CANCELLED"));

    placeOrder(line(tShirt, 2)).andExpect(status().isCreated());
  }

  @Test
  void shouldNotShipUnpaidOrder() throws Exception {
    var orderId = idOf(placeOrder(line(addProduct("19.99", 2), 1)));

    move(orderId, "shipment").andExpect(status().isConflict());
  }

  @Test
  void shouldNotViewUnknownOrder() throws Exception {
    rest.perform(get("/api/ecommerce/orders/" + UUID.randomUUID())).andExpect(status().isNotFound());
  }

  private String addProduct(String price, int stock) throws Exception {
    var body = "{\"name\":\"Product %s\",\"unitPrice\":%s,\"stock\":%d}".formatted(UUID.randomUUID().toString().substring(0, 8), price, stock);

    return idOf(rest.perform(post("/api/ecommerce/products").contentType(MediaType.APPLICATION_JSON).content(body)));
  }

  private ResultActions placeOrder(String lines) throws Exception {
    var body = "{\"customerEmail\":\"Jane@Shop.com\",\"lines\":[" + lines + "]}";

    return rest.perform(post("/api/ecommerce/orders").contentType(MediaType.APPLICATION_JSON).content(body));
  }

  private ResultActions move(String orderId, String step) throws Exception {
    return rest.perform(post("/api/ecommerce/orders/" + orderId + "/" + step));
  }

  private String idOf(ResultActions result) throws Exception {
    return JsonPath.read(result.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), "$.id");
  }

  private static String line(String productId, int quantity) {
    return "{\"productId\":\"" + productId + "\",\"quantity\":" + quantity + "}";
  }
}
