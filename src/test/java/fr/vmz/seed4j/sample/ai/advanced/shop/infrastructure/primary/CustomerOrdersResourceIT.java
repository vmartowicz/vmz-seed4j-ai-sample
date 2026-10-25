package fr.vmz.seed4j.sample.ai.advanced.shop.infrastructure.primary;

import static fr.vmz.seed4j.sample.ai.advanced.shop.infrastructure.primary.ShopApi.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@AutoConfigureMockMvc
@Import(MovableClockConfiguration.class)
class CustomerOrdersResourceIT {

  private static final LocalDate MARCH_10 = LocalDate.of(2026, 3, 10);

  // In-memory data is shared by the whole Spring context: each test uses its own articles and customers
  @Autowired
  private MockMvc rest;

  @Autowired
  private MovableClock clock;

  private ShopApi api;

  @BeforeEach
  void loadApi() {
    api = new ShopApi(rest);
    clock.today(MARCH_10);
  }

  @Nested
  class Place {

    @Test
    void shouldPlaceOrderWithShippingFee() throws Exception {
      var articleId = api.articleId("25", 10);
      var customer = uniqueCustomer();

      api
        .placeOrder(customer.toUpperCase(), line(articleId, 2))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.customerEmail").value(customer))
        .andExpect(jsonPath("$.status").value("AWAITING_PAYMENT"))
        .andExpect(jsonPath("$.lines[0].articleId").value(articleId))
        .andExpect(jsonPath("$.lines[0].quantity").value(2))
        .andExpect(jsonPath("$.lines[0].unitPrice").value(25.0))
        .andExpect(jsonPath("$.itemsTotal").value(50.0))
        .andExpect(jsonPath("$.discount").value(0.0))
        .andExpect(jsonPath("$.shipping").value(5.0))
        .andExpect(jsonPath("$.total").value(55.0))
        .andExpect(jsonPath("$.history[0].status").value("AWAITING_PAYMENT"))
        .andExpect(jsonPath("$.history[0].at").value("2026-03-10T10:00:00"))
        .andExpect(jsonPath("$.trackingNumber").doesNotExist())
        .andExpect(jsonPath("$.refund").doesNotExist())
        .andExpect(jsonPath("$.returnRequested").value(false));
    }

    @Test
    void shouldOfferShippingAbove50Euros() throws Exception {
      api.placeOrder(uniqueCustomer(), line(api.articleId("50.01", 10), 1)).andExpect(jsonPath("$.shipping").value(0.0));
    }

    @Test
    void shouldReserveStock() throws Exception {
      var articleId = api.articleId("25", 3);
      api.placeOrder(uniqueCustomer(), line(articleId, 2)).andExpect(status().isCreated());

      api.placeOrder(uniqueCustomer(), line(articleId, 2)).andExpect(status().isConflict());
    }

    @Test
    void shouldNotPlaceOrderForUnknownArticle() throws Exception {
      api.placeOrder(uniqueCustomer(), line(UUID.randomUUID().toString(), 1)).andExpect(status().isNotFound());
    }

    @Test
    void shouldNotPlaceOrderWithTwoLinesOfTheSameArticle() throws Exception {
      var articleId = api.articleId("25", 10);

      api.placeOrder(uniqueCustomer(), line(articleId, 1) + "," + line(articleId, 1)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotPlaceOrderWithInvalidEmail() throws Exception {
      api.placeOrder("jane@shop", line(api.articleId("25", 10), 1)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldDiscountSixthOrderOfLoyalCustomer() throws Exception {
      var articleId = api.articleId("20", 100);
      var customer = uniqueCustomer();
      for (var index = 0; index < 5; index++) {
        api.deliver(api.orderId(customer, line(articleId, 1)));
      }

      api
        .placeOrder(customer, line(articleId, 1))
        .andExpect(jsonPath("$.discount").value(2.0))
        .andExpect(jsonPath("$.total").value(23.0));
    }
  }

  @Test
  void shouldTrackOrderUntilDelivery() throws Exception {
    var orderId = api.orderId(uniqueCustomer(), line(api.articleId("25", 10), 1));

    api.deliver(orderId);

    rest
      .perform(get("/api/shop/orders/" + orderId))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value("DELIVERED"))
      .andExpect(jsonPath("$.trackingNumber").value("6A12345678901"))
      .andExpect(jsonPath("$.deliveredOn").value("2026-03-10"))
      .andExpect(jsonPath("$.history[*].status", contains("AWAITING_PAYMENT", "PAID", "IN_PREPARATION", "SHIPPED", "DELIVERED")));
  }

  @Test
  void shouldNotShipWithoutTrackingNumber() throws Exception {
    var orderId = api.orderId(uniqueCustomer(), line(api.articleId("25", 10), 1));
    api.step(orderId, "payment");
    api.step(orderId, "preparation");

    api.step(orderId, "shipment").andExpect(status().isBadRequest());
  }

  @Test
  void shouldRefundCancelledPaidOrderAndReleaseStock() throws Exception {
    var articleId = api.articleId("60", 1);
    var orderId = api.orderId(uniqueCustomer(), line(articleId, 1));
    api.step(orderId, "payment");

    api
      .step(orderId, "cancellation")
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value("CANCELLED"))
      .andExpect(jsonPath("$.refund.amount").value(60.0))
      .andExpect(jsonPath("$.refund.dueBy").value("2026-03-15"));
    api.placeOrder(uniqueCustomer(), line(articleId, 1)).andExpect(status().isCreated());
  }

  @Test
  void shouldNotCancelShippedOrder() throws Exception {
    var orderId = api.orderId(uniqueCustomer(), line(api.articleId("25", 10), 1));
    api.step(orderId, "payment");
    api.step(orderId, "preparation");
    api.ship(orderId);

    api.step(orderId, "cancellation").andExpect(status().isConflict());
  }

  @Test
  void shouldReturnDeliveredOrder() throws Exception {
    var articleId = api.articleId("20", 1);
    var orderId = api.orderId(uniqueCustomer(), line(articleId, 1));
    api.deliver(orderId);
    clock.today(MARCH_10.plusDays(14));

    api.step(orderId, "return-request").andExpect(status().isOk()).andExpect(jsonPath("$.returnRequested").value(true));
    api
      .step(orderId, "return-reception")
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value("RETURNED"))
      .andExpect(jsonPath("$.refund.amount").value(20.0));
    api.placeOrder(uniqueCustomer(), line(articleId, 1)).andExpect(status().isCreated());
  }

  @Test
  void shouldNotReturnOrder15DaysAfterDelivery() throws Exception {
    var orderId = api.orderId(uniqueCustomer(), line(api.articleId("20", 1), 1));
    api.deliver(orderId);
    clock.today(MARCH_10.plusDays(15));

    api.step(orderId, "return-request").andExpect(status().isConflict()).andExpect(jsonPath("$.title").value("Business rule violation"));
  }

  @Test
  void shouldNotFollowUnknownOrder() throws Exception {
    rest.perform(get("/api/shop/orders/" + UUID.randomUUID())).andExpect(status().isNotFound());
  }
}
