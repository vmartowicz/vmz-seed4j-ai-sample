package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order;

import static fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.OrdersFixture.*;
import static fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.ProductsFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.Money;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.InsufficientStockException;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.Product;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.ProductId;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.ProductsRepository;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.Stock;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.product.UnknownProductException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class OrdersDeskTest {

  private final ProductsRepository products = mock(ProductsRepository.class, CALLS_REAL_METHODS);
  private final OrdersRepository orders = mock(OrdersRepository.class, CALLS_REAL_METHODS);
  private final OrderNotifier notifier = mock(OrderNotifier.class);
  private final OrdersDesk desk = new OrdersDesk(products, orders, notifier);

  @Nested
  class Place {

    @Test
    void shouldPlaceOrderAtCatalogPricesAndReserveStock() {
      var tShirt = existing(tShirt(10));
      var mug = existing(mug(1));

      var order = desk.place(jane(), List.of(new LineToPlace(tShirt.id(), new Quantity(2)), new LineToPlace(mug.id(), new Quantity(1))));

      assertThat(order.total()).isEqualTo(new Money(new BigDecimal("48.48")));
      assertThat(tShirt.stock()).isEqualTo(new Stock(8));
      assertThat(mug.stock()).isEqualTo(new Stock(0));
      verify(products).save(tShirt);
      verify(products).save(mug);
      verify(orders).save(order);
      verify(notifier).statusChanged(new OrderStatusChanged(order.id(), jane(), OrderStatus.PENDING));
    }

    @Test
    void shouldNotPlaceOrderWithInsufficientStock() {
      var tShirt = existing(tShirt(10));
      var mug = existing(mug(1));

      assertThatThrownBy(() ->
        desk.place(jane(), List.of(new LineToPlace(tShirt.id(), new Quantity(2)), new LineToPlace(mug.id(), new Quantity(2))))
      ).isExactlyInstanceOf(InsufficientStockException.class);
      assertThat(tShirt.stock()).isEqualTo(new Stock(10));
      verify(products, never()).save(any());
      verify(orders, never()).save(any());
      verify(notifier, never()).statusChanged(any());
    }

    @Test
    void shouldNotPlaceOrderForUnknownProduct() {
      var unknown = ProductId.newId();
      doReturn(Optional.empty()).when(products).get(unknown);

      assertThatThrownBy(() -> desk.place(jane(), List.of(new LineToPlace(unknown, new Quantity(1)))))
        .isExactlyInstanceOf(UnknownProductException.class)
        .hasMessageContaining(unknown.get().toString());
    }
  }

  @Test
  void shouldNotifyPayment() {
    var order = existing(orderIn(OrderStatus.PENDING));

    desk.pay(order.id());

    assertThat(order.status()).isEqualTo(OrderStatus.PAID);
    verify(orders).save(order);
    verify(notifier).statusChanged(new OrderStatusChanged(order.id(), jane(), OrderStatus.PAID));
  }

  @Test
  void shouldNotifyShipmentAndDelivery() {
    var order = existing(orderIn(OrderStatus.PAID));

    desk.ship(order.id());
    desk.deliver(order.id(), TODAY);

    assertThat(order.deliveredOn()).contains(TODAY);
    verify(notifier).statusChanged(new OrderStatusChanged(order.id(), jane(), OrderStatus.SHIPPED));
    verify(notifier).statusChanged(new OrderStatusChanged(order.id(), jane(), OrderStatus.DELIVERED));
  }

  @Test
  void shouldReleaseStockOnCancellation() {
    var tShirt = existing(tShirt(8));
    var order = existing(new OrderToCreate(jane(), List.of(line(tShirt.id(), 2, "19.99"))).create());

    desk.cancel(order.id());

    assertThat(tShirt.stock()).isEqualTo(new Stock(10));
    verify(products).save(tShirt);
    verify(notifier).statusChanged(new OrderStatusChanged(order.id(), jane(), OrderStatus.CANCELLED));
  }

  @Test
  void shouldReleaseStockOnReturn() {
    var tShirt = existing(tShirt(8));
    var order = new OrderToCreate(jane(), List.of(line(tShirt.id(), 2, "19.99"))).create();
    order.pay();
    order.ship();
    order.deliver(TODAY);
    existing(order);

    desk.returnOrder(order.id(), TODAY.plusDays(3));

    assertThat(tShirt.stock()).isEqualTo(new Stock(10));
    verify(notifier).statusChanged(new OrderStatusChanged(order.id(), jane(), OrderStatus.RETURNED));
  }

  @Test
  void shouldNotReleaseStockOfOrderThatCantBeCancelled() {
    var order = existing(orderIn(OrderStatus.SHIPPED));

    assertThatThrownBy(() -> desk.cancel(order.id())).isExactlyInstanceOf(InvalidOrderTransitionException.class);
    verify(products, never()).save(any());
  }

  @Test
  void shouldNotHandleUnknownOrder() {
    var unknown = OrderId.newId();
    doReturn(Optional.empty()).when(orders).get(unknown);

    assertThatThrownBy(() -> desk.pay(unknown))
      .isExactlyInstanceOf(UnknownOrderException.class)
      .hasMessageContaining(unknown.get().toString());
  }

  private Product existing(Product product) {
    doReturn(Optional.of(product)).when(products).get(product.id());

    return product;
  }

  private Order existing(Order order) {
    doReturn(Optional.of(order)).when(orders).get(order.id());

    return order;
  }
}
