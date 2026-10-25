package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order;

import static fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.OrderStatus.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

@UnitTest
class OrderStatusTest {

  @Test
  void shouldMovePendingOrderToPaidOrCancelled() {
    assertThat(nextOf(PENDING)).containsExactly(PAID, CANCELLED);
  }

  @Test
  void shouldMovePaidOrderToShippedOrCancelled() {
    assertThat(nextOf(PAID)).containsExactly(SHIPPED, CANCELLED);
  }

  @Test
  void shouldMoveShippedOrderToDeliveredOnly() {
    assertThat(nextOf(SHIPPED)).containsExactly(DELIVERED);
  }

  @Test
  void shouldMoveDeliveredOrderToReturnedOnly() {
    assertThat(nextOf(DELIVERED)).containsExactly(RETURNED);
  }

  @Test
  void shouldNotMoveCancelledOrReturnedOrders() {
    assertThat(nextOf(CANCELLED)).isEmpty();
    assertThat(nextOf(RETURNED)).isEmpty();
  }

  private static Stream<OrderStatus> nextOf(OrderStatus status) {
    return Stream.of(OrderStatus.values()).filter(status::canMoveTo);
  }
}
