package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.infrastructure.secondary.order;

import static fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.OrdersFixture.*;

import ch.qos.logback.classic.Level;
import fr.vmz.seed4j.sample.ai.Logs;
import fr.vmz.seed4j.sample.ai.LogsSpy;
import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.OrderId;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.OrderStatus;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.OrderStatusChanged;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@UnitTest
class LoggingOrderNotifierTest {

  @Logs
  private LogsSpy logs;

  @Test
  void shouldLogStatusChangeEmail() {
    var order = new OrderId(UUID.fromString("00000000-0000-4000-8000-000000000042"));

    new LoggingOrderNotifier().statusChanged(new OrderStatusChanged(order, jane(), OrderStatus.SHIPPED));

    logs.shouldHave(Level.INFO, "Email to jane@shop.com: your order 00000000-0000-4000-8000-000000000042 is now SHIPPED");
  }
}
