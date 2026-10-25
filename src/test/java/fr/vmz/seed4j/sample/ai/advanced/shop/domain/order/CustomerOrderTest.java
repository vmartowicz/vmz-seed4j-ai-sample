package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import static fr.vmz.seed4j.sample.ai.advanced.shop.domain.MoneyFixture.money;
import static fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.CustomerOrdersFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticleId;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class CustomerOrderTest {

  @Test
  void shouldPlaceOrderAwaitingPayment() {
    var order = order();

    assertThat(order.id()).isNotNull();
    assertThat(order.customer()).isEqualTo(jane());
    assertThat(order.lines()).hasSize(1);
    assertThat(order.pricing().total()).isEqualTo(money("60.00"));
    assertThat(order.status()).isEqualTo(OrderStatus.AWAITING_PAYMENT);
    assertThat(order.history()).containsExactly(new StatusChange(OrderStatus.AWAITING_PAYMENT, NOW));
    assertThat(order.trackingNumber()).isEmpty();
    assertThat(order.deliveredOn()).isEmpty();
    assertThat(order.returnRequested()).isFalse();
    assertThat(order.refund()).isEmpty();
  }

  @Test
  void shouldNotPlaceOrderWithoutLines() {
    assertThatThrownBy(() -> new OrderToPlace(jane(), List.of())).isExactlyInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldNotPlaceOrderWithTwoLinesOfTheSameArticle() {
    var article = ArticleId.newId();

    assertThatThrownBy(() -> new OrderToPlace(jane(), List.of(line(article, 1, "10"), line(article, 1, "10"))))
      .isExactlyInstanceOf(DuplicatedOrderLineException.class)
      .hasMessageContaining(article.get().toString());
  }

  @Test
  void shouldFollowOrderUntilDelivery() {
    var order = deliveredOrder();

    assertThat(order.status()).isEqualTo(OrderStatus.DELIVERED);
    assertThat(order.trackingNumber()).contains(new TrackingNumber("6A12345678901"));
    assertThat(order.deliveredOn()).contains(NOW.toLocalDate());
    assertThat(order.history())
      .extracting(StatusChange::status)
      .containsExactly(OrderStatus.AWAITING_PAYMENT, OrderStatus.PAID, OrderStatus.IN_PREPARATION, OrderStatus.SHIPPED, OrderStatus.DELIVERED);
  }

  @Test
  void shouldNotShipOrderNotPrepared() {
    var order = order();
    order.pay(NOW);

    assertThatThrownBy(() -> order.ship(new TrackingNumber("6A12345678901"), NOW))
      .isExactlyInstanceOf(InvalidOrderTransitionException.class)
      .hasMessageContaining("PAID")
      .hasMessageContaining("SHIPPED");
    assertThat(order.trackingNumber()).isEmpty();
  }

  @Nested
  class Cancel {

    @Test
    void shouldCancelUnpaidOrderWithoutRefund() {
      var order = order();

      order.cancel(NOW);

      assertThat(order.status()).isEqualTo(OrderStatus.CANCELLED);
      assertThat(order.refund()).isEmpty();
    }

    @Test
    void shouldRefundPaidOrderWithin5Days() {
      var order = order();
      order.pay(NOW);
      order.prepare(NOW);

      order.cancel(NOW);

      assertThat(order.refund()).contains(new Refund(money("60.00"), NOW.toLocalDate().plusDays(5)));
    }

    @Test
    void shouldNotCancelShippedOrder() {
      var order = order();
      order.pay(NOW);
      order.prepare(NOW);
      order.ship(new TrackingNumber("6A12345678901"), NOW);

      assertThatThrownBy(() -> order.cancel(NOW)).isExactlyInstanceOf(InvalidOrderTransitionException.class);
      assertThat(order.refund()).isEmpty();
    }
  }

  @Nested
  class Return {

    @Test
    void shouldRequestReturn14DaysAfterDelivery() {
      var order = deliveredOrder();

      order.requestReturn(NOW.plusDays(14));

      assertThat(order.returnRequested()).isTrue();
      assertThat(order.status()).isEqualTo(OrderStatus.DELIVERED);
    }

    @Test
    void shouldNotRequestReturn15DaysAfterDelivery() {
      var order = deliveredOrder();

      assertThatThrownBy(() -> order.requestReturn(NOW.plusDays(15)))
        .isExactlyInstanceOf(ReturnPeriodExpiredException.class)
        .hasMessageContaining("2026-03-10");
      assertThat(order.returnRequested()).isFalse();
    }

    @Test
    void shouldNotRequestReturnOfUndeliveredOrder() {
      assertThatThrownBy(() -> order().requestReturn(NOW)).isExactlyInstanceOf(OrderNotDeliveredException.class);
    }

    @Test
    void shouldNotRequestReturnTwice() {
      var order = deliveredOrder();
      order.requestReturn(NOW);

      assertThatThrownBy(() -> order.requestReturn(NOW)).isExactlyInstanceOf(ReturnAlreadyRequestedException.class);
    }

    @Test
    void shouldReceiveReturnAndRefundItemsOnly() {
      var order = orderOf("20.00", 1);
      order.pay(NOW);
      order.prepare(NOW);
      order.ship(new TrackingNumber("6A12345678901"), NOW);
      order.deliver(NOW);
      order.requestReturn(NOW.plusDays(2));

      order.receiveReturn(NOW.plusDays(4));

      assertThat(order.status()).isEqualTo(OrderStatus.RETURNED);
      assertThat(order.pricing().total()).isEqualTo(money("25.00"));
      assertThat(order.refund()).contains(new Refund(money("20.00"), NOW.toLocalDate().plusDays(9)));
    }

    @Test
    void shouldNotReceiveReturnNotRequested() {
      var order = deliveredOrder();

      assertThatThrownBy(() -> order.receiveReturn(NOW))
        .isExactlyInstanceOf(ReturnNotRequestedException.class)
        .hasMessageContaining(order.id().get().toString());
    }
  }
}
