package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import static fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.OrderStatus.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

@UnitTest
class OrderStatusTest {

  @Test
  void shouldFollowOrderSteps() {
    assertThat(nextOf(AWAITING_PAYMENT)).containsExactly(PAID, CANCELLED);
    assertThat(nextOf(PAID)).containsExactly(IN_PREPARATION, CANCELLED);
    assertThat(nextOf(IN_PREPARATION)).containsExactly(SHIPPED, CANCELLED);
    assertThat(nextOf(SHIPPED)).containsExactly(DELIVERED);
    assertThat(nextOf(DELIVERED)).containsExactly(RETURNED);
  }

  @Test
  void shouldNotMoveReturnedOrCancelledOrders() {
    assertThat(nextOf(RETURNED)).isEmpty();
    assertThat(nextOf(CANCELLED)).isEmpty();
  }

  private static Stream<OrderStatus> nextOf(OrderStatus status) {
    return Stream.of(OrderStatus.values()).filter(status::canMoveTo);
  }
}
