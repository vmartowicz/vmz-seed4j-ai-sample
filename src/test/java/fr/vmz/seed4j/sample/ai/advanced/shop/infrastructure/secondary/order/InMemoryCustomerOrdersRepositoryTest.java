package fr.vmz.seed4j.sample.ai.advanced.shop.infrastructure.secondary.order;

import static fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.CustomerOrdersFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticleId;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.CustomerEmail;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.CustomerOrderId;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.OrderToPlace;
import java.util.List;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryCustomerOrdersRepositoryTest {

  private final InMemoryCustomerOrdersRepository orders = new InMemoryCustomerOrdersRepository();

  @Test
  void shouldGetOrdersOfCustomer() {
    var janes = order();
    var johns = new OrderToPlace(new CustomerEmail("john@shop.com"), List.of(line(ArticleId.newId(), 1, "10"))).place(false, NOW);
    orders.save(janes);
    orders.save(johns);

    assertThat(orders.get(janes.id())).contains(janes);
    assertThat(orders.get(CustomerOrderId.newId())).isEmpty();
    assertThat(orders.ofCustomer(jane())).containsExactly(janes);
  }
}
