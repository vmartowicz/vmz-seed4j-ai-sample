package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order;

import static fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.OrdersFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.Money;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.ProductId;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class OrderTest {

  @Test
  void shouldCreatePendingOrder() {
    var order = order();

    assertThat(order.id()).isNotNull();
    assertThat(order.customer()).isEqualTo(jane());
    assertThat(order.lines()).hasSize(2);
    assertThat(order.status()).isEqualTo(OrderStatus.PENDING);
    assertThat(order.deliveredOn()).isEmpty();
  }

  @Test
  void shouldSumLinesInTotal() {
    assertThat(order().total()).isEqualTo(new Money(new BigDecimal("48.48")));
  }

  @Test
  void shouldNotCreateOrderWithoutLines() {
    assertThatThrownBy(() -> new OrderToCreate(jane(), List.of())).isExactlyInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldNotCreateOrderWithTwoLinesOfTheSameProduct() {
    var product = ProductId.newId();

    assertThatThrownBy(() -> new OrderToCreate(jane(), List.of(line(product, 1, "10"), line(product, 2, "10"))))
      .isExactlyInstanceOf(DuplicatedOrderLineException.class)
      .hasMessageContaining(product.get().toString());
  }

  @Nested
  class Lifecycle {

    @Test
    void shouldPayShipAndDeliverOrder() {
      var order = order();

      order.pay();
      order.ship();
      order.deliver(TODAY);

      assertThat(order.status()).isEqualTo(OrderStatus.DELIVERED);
      assertThat(order.deliveredOn()).contains(TODAY);
    }

    @Test
    void shouldNotShipUnpaidOrder() {
      var order = order();

      assertThatThrownBy(order::ship)
        .isExactlyInstanceOf(InvalidOrderTransitionException.class)
        .hasMessageContaining("PENDING")
        .hasMessageContaining("SHIPPED");
      assertThat(order.status()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    void shouldCancelPendingOrder() {
      var order = orderIn(OrderStatus.PENDING);

      order.cancel();

      assertThat(order.status()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    void shouldCancelPaidOrder() {
      var order = orderIn(OrderStatus.PAID);

      order.cancel();

      assertThat(order.status()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    void shouldNotCancelShippedOrder() {
      var order = orderIn(OrderStatus.SHIPPED);

      assertThatThrownBy(order::cancel).isExactlyInstanceOf(InvalidOrderTransitionException.class);
    }
  }

  @Nested
  class Return {

    @Test
    void shouldReturnOrder14DaysAfterDelivery() {
      var order = orderIn(OrderStatus.DELIVERED);

      order.returnOrder(TODAY.plusDays(14));

      assertThat(order.status()).isEqualTo(OrderStatus.RETURNED);
    }

    @Test
    void shouldNotReturnOrder15DaysAfterDelivery() {
      var order = orderIn(OrderStatus.DELIVERED);

      assertThatThrownBy(() -> order.returnOrder(TODAY.plusDays(15)))
        .isExactlyInstanceOf(ReturnPeriodExpiredException.class)
        .hasMessageContaining(TODAY.toString());
      assertThat(order.status()).isEqualTo(OrderStatus.DELIVERED);
    }

    @Test
    void shouldNotReturnUndeliveredOrder() {
      var order = orderIn(OrderStatus.SHIPPED);

      assertThatThrownBy(() -> order.returnOrder(TODAY)).isExactlyInstanceOf(InvalidOrderTransitionException.class);
    }
  }
}
