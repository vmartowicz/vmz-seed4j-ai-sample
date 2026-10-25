package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.infrastructure.secondary.order;

import static fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.OrdersFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.OrderId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryOrdersRepositoryTest {

  private final InMemoryOrdersRepository orders = new InMemoryOrdersRepository();

  @Test
  void shouldGetSavedOrder() {
    var order = order();
    orders.save(order);

    assertThat(orders.get(order.id())).contains(order);
  }

  @Test
  void shouldNotGetUnknownOrder() {
    assertThat(orders.get(OrderId.newId())).isEmpty();
  }
}
