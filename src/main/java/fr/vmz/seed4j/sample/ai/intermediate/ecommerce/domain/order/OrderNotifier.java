package fr.vmz.seed4j.sample.ai.intermediate.ecommerce.domain.order;

public interface OrderNotifier {
  void statusChanged(OrderStatusChanged change);
}
