package fr.vmz.seed4j.sample.ai.advanced.shop.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.shop.application.CustomerOrdersApplicationService;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.CustomerOrderId;
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
@RequestMapping("/api/shop/orders")
class CustomerOrdersResource {

  private final CustomerOrdersApplicationService orders;

  CustomerOrdersResource(CustomerOrdersApplicationService orders) {
    this.orders = orders;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  RestCustomerOrder place(@RequestBody RestOrderToPlace order) {
    return RestCustomerOrder.from(orders.place(order.customer(), order.linesToOrder()));
  }

  @GetMapping("/{orderId}")
  RestCustomerOrder get(@PathVariable UUID orderId) {
    return RestCustomerOrder.from(orders.get(new CustomerOrderId(orderId)));
  }

  @PostMapping("/{orderId}/payment")
  RestCustomerOrder pay(@PathVariable UUID orderId) {
    return RestCustomerOrder.from(orders.pay(new CustomerOrderId(orderId)));
  }

  @PostMapping("/{orderId}/preparation")
  RestCustomerOrder prepare(@PathVariable UUID orderId) {
    return RestCustomerOrder.from(orders.prepare(new CustomerOrderId(orderId)));
  }

  @PostMapping("/{orderId}/shipment")
  RestCustomerOrder ship(@PathVariable UUID orderId, @RequestBody RestShipment shipment) {
    return RestCustomerOrder.from(orders.ship(new CustomerOrderId(orderId), shipment.toDomain()));
  }

  @PostMapping("/{orderId}/delivery")
  RestCustomerOrder deliver(@PathVariable UUID orderId) {
    return RestCustomerOrder.from(orders.deliver(new CustomerOrderId(orderId)));
  }

  @PostMapping("/{orderId}/cancellation")
  RestCustomerOrder cancel(@PathVariable UUID orderId) {
    return RestCustomerOrder.from(orders.cancel(new CustomerOrderId(orderId)));
  }

  @PostMapping("/{orderId}/return-request")
  RestCustomerOrder requestReturn(@PathVariable UUID orderId) {
    return RestCustomerOrder.from(orders.requestReturn(new CustomerOrderId(orderId)));
  }

  @PostMapping("/{orderId}/return-reception")
  RestCustomerOrder receiveReturn(@PathVariable UUID orderId) {
    return RestCustomerOrder.from(orders.receiveReturn(new CustomerOrderId(orderId)));
  }
}
