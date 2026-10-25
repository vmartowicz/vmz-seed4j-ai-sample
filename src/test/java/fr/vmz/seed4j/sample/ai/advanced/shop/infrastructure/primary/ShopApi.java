package fr.vmz.seed4j.sample.ai.advanced.shop.infrastructure.primary;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

final class ShopApi {

  private final MockMvc rest;

  ShopApi(MockMvc rest) {
    this.rest = rest;
  }

  ResultActions addArticle(String name, String price, int stock) throws Exception {
    return postJson("/api/shop/articles", "{\"name\":\"%s\",\"unitPrice\":%s,\"stock\":%d}".formatted(name, price, stock));
  }

  String articleId(String price, int stock) throws Exception {
    return idOf(addArticle(uniqueName(), price, stock));
  }

  ResultActions placeOrder(String customer, String lines) throws Exception {
    return postJson("/api/shop/orders", "{\"customerEmail\":\"" + customer + "\",\"lines\":[" + lines + "]}");
  }

  String orderId(String customer, String lines) throws Exception {
    return idOf(placeOrder(customer, lines));
  }

  ResultActions step(String orderId, String step) throws Exception {
    return postJson("/api/shop/orders/" + orderId + "/" + step, "{}");
  }

  ResultActions ship(String orderId) throws Exception {
    return postJson("/api/shop/orders/" + orderId + "/shipment", "{\"trackingNumber\":\"6A12345678901\"}");
  }

  void deliver(String orderId) throws Exception {
    step(orderId, "payment").andExpect(status().isOk());
    step(orderId, "preparation").andExpect(status().isOk());
    ship(orderId).andExpect(status().isOk());
    step(orderId, "delivery").andExpect(status().isOk());
  }

  ResultActions postJson(String url, String body) throws Exception {
    return rest.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(body));
  }

  static String line(String articleId, int quantity) {
    return "{\"articleId\":\"" + articleId + "\",\"quantity\":" + quantity + "}";
  }

  static String uniqueName() {
    return "Article " + UUID.randomUUID().toString().substring(0, 8);
  }

  static String uniqueCustomer() {
    return "customer-" + UUID.randomUUID().toString().substring(0, 8) + "@shop.com";
  }

  private static String idOf(ResultActions result) throws Exception {
    return JsonPath.read(result.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), "$.id");
  }
}
