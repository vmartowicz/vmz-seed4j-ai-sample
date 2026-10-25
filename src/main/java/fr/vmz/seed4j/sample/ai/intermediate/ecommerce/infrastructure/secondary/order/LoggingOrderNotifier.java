package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.infrastructure.secondary.order;

import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.OrderNotifier;
import fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order.OrderStatusChanged;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class LoggingOrderNotifier implements OrderNotifier {

  private static final Logger log = LoggerFactory.getLogger(LoggingOrderNotifier.class);

  @Override
  public void statusChanged(OrderStatusChanged change) {
    log.info("Email to {}: your order {} is now {}", change.customer().get(), change.order().get(), change.status());
  }
}
