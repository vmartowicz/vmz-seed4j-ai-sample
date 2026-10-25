package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.application.OrdersApplicationService;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.OrderId;
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
@RequestMapping("/api/ecommerce/orders")
class OrdersResource {

  private final OrdersApplicationService orders;

  OrdersResource(OrdersApplicationService orders) {
    this.orders = orders;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  RestOrder place(@RequestBody RestOrderToPlace order) {
    return RestOrder.from(orders.place(order.customer(), order.linesToPlace()));
  }

  @GetMapping("/{orderId}")
  RestOrder get(@PathVariable UUID orderId) {
    return RestOrder.from(orders.get(new OrderId(orderId)));
  }

  @PostMapping("/{orderId}/payment")
  RestOrder pay(@PathVariable UUID orderId) {
    return RestOrder.from(orders.pay(new OrderId(orderId)));
  }

  @PostMapping("/{orderId}/shipment")
  RestOrder ship(@PathVariable UUID orderId) {
    return RestOrder.from(orders.ship(new OrderId(orderId)));
  }

  @PostMapping("/{orderId}/delivery")
  RestOrder deliver(@PathVariable UUID orderId) {
    return RestOrder.from(orders.deliver(new OrderId(orderId)));
  }

  @PostMapping("/{orderId}/cancellation")
  RestOrder cancel(@PathVariable UUID orderId) {
    return RestOrder.from(orders.cancel(new OrderId(orderId)));
  }

  @PostMapping("/{orderId}/return")
  RestOrder returnOrder(@PathVariable UUID orderId) {
    return RestOrder.from(orders.returnOrder(new OrderId(orderId)));
  }
}
