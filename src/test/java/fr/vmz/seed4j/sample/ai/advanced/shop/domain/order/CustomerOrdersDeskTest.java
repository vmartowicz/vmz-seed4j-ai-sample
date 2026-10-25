package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import static fr.vmz.seed4j.sample.ai.advanced.shop.domain.MoneyFixture.money;
import static fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticlesFixture.*;
import static fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.CustomerOrdersFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.Money;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.Article;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticleId;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticlesRepository;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.InsufficientStockException;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.Stock;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.UnknownArticleException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class CustomerOrdersDeskTest {

  private final ArticlesRepository articles = mock(ArticlesRepository.class, CALLS_REAL_METHODS);
  private final CustomerOrdersRepository orders = mock(CustomerOrdersRepository.class, CALLS_REAL_METHODS);
  private final CustomerOrdersDesk desk = new CustomerOrdersDesk(articles, orders);

  private Article tShirt;
  private Article scarf;

  @BeforeEach
  void loadArticles() {
    tShirt = existing(tShirt(10));
    scarf = existing(scarf(1));
    doReturn(List.of()).when(orders).ofCustomer(any());
  }

  @Nested
  class Place {

    @Test
    void shouldPlaceOrderAtCatalogPricesAndReserveStock() {
      var order = desk.place(jane(), List.of(new LineToOrder(tShirt.id(), new Quantity(2)), new LineToOrder(scarf.id(), new Quantity(1))), NOW);

      assertThat(order.pricing().items()).isEqualTo(money("75.00"));
      assertThat(order.pricing().total()).isEqualTo(money("75.00"));
      assertThat(tShirt.stock()).isEqualTo(new Stock(8));
      assertThat(scarf.stock()).isEqualTo(new Stock(0));
      verify(articles).save(tShirt);
      verify(articles).save(scarf);
      verify(orders).save(order);
    }

    @Test
    void shouldNotPlaceOrderWithInsufficientStock() {
      assertThatThrownBy(() ->
        desk.place(jane(), List.of(new LineToOrder(tShirt.id(), new Quantity(2)), new LineToOrder(scarf.id(), new Quantity(2))), NOW)
      ).isExactlyInstanceOf(InsufficientStockException.class);
      assertThat(tShirt.stock()).isEqualTo(new Stock(10));
      verify(articles, never()).save(any());
      verify(orders, never()).save(any());
    }

    @Test
    void shouldNotPlaceOrderForUnknownArticle() {
      var unknown = ArticleId.newId();
      doReturn(Optional.empty()).when(articles).get(unknown);

      assertThatThrownBy(() -> desk.place(jane(), List.of(new LineToOrder(unknown, new Quantity(1))), NOW)).isExactlyInstanceOf(
        UnknownArticleException.class
      );
    }

    @Test
    void shouldDiscountOrdersOfCustomersWith5DeliveredOrders() {
      doReturn(delivered(5)).when(orders).ofCustomer(jane());

      var order = desk.place(jane(), List.of(new LineToOrder(tShirt.id(), new Quantity(1))), NOW);

      assertThat(order.pricing().discount()).isEqualTo(money("2.00"));
    }

    @Test
    void shouldNotCountReturnedOrdersForLoyalty() {
      var history = delivered(4);
      var returned = deliveredOrder();
      returned.requestReturn(NOW);
      returned.receiveReturn(NOW);
      history.add(returned);
      history.add(order());
      doReturn(history).when(orders).ofCustomer(jane());

      var order = desk.place(jane(), List.of(new LineToOrder(tShirt.id(), new Quantity(1))), NOW);

      assertThat(order.pricing().discount()).isEqualTo(Money.ZERO);
    }

    private List<CustomerOrder> delivered(int count) {
      return new ArrayList<>(IntStream.range(0, count).mapToObj(index -> deliveredOrder()).toList());
    }
  }

  @Test
  void shouldFollowOrderUntilDelivery() {
    var order = existing(order());

    desk.pay(order.id(), NOW);
    desk.prepare(order.id(), NOW);
    desk.ship(order.id(), new TrackingNumber("6A12345678901"), NOW);
    desk.deliver(order.id(), NOW);

    assertThat(order.status()).isEqualTo(OrderStatus.DELIVERED);
    verify(orders, times(4)).save(order);
  }

  @Test
  void shouldReleaseStockOnCancellation() {
    var order = existing(new OrderToPlace(jane(), List.of(line(tShirt.id(), 2, "20.00"))).place(false, NOW));

    desk.cancel(order.id(), NOW);

    assertThat(tShirt.stock()).isEqualTo(new Stock(12));
    verify(articles).save(tShirt);
  }

  @Test
  void shouldReleaseStockOnReturnReception() {
    var order = new OrderToPlace(jane(), List.of(line(tShirt.id(), 2, "20.00"))).place(false, NOW);
    order.pay(NOW);
    order.prepare(NOW);
    order.ship(new TrackingNumber("6A12345678901"), NOW);
    order.deliver(NOW);
    existing(order);

    desk.requestReturn(order.id(), NOW.plusDays(1));
    desk.receiveReturn(order.id(), NOW.plusDays(3));

    assertThat(order.status()).isEqualTo(OrderStatus.RETURNED);
    assertThat(tShirt.stock()).isEqualTo(new Stock(12));
  }

  @Test
  void shouldNotReleaseStockOfOrderThatCantBeCancelled() {
    var order = existing(deliveredOrder());

    assertThatThrownBy(() -> desk.cancel(order.id(), NOW)).isExactlyInstanceOf(InvalidOrderTransitionException.class);
    verify(articles, never()).save(any());
  }

  @Test
  void shouldNotHandleUnknownOrder() {
    var unknown = CustomerOrderId.newId();
    doReturn(Optional.empty()).when(orders).get(unknown);

    assertThatThrownBy(() -> desk.pay(unknown, NOW))
      .isExactlyInstanceOf(UnknownCustomerOrderException.class)
      .hasMessageContaining(unknown.get().toString());
  }

  private Article existing(Article article) {
    doReturn(Optional.of(article)).when(articles).get(article.id());

    return article;
  }

  private CustomerOrder existing(CustomerOrder order) {
    doReturn(Optional.of(order)).when(orders).get(order.id());

    return order;
  }
}
