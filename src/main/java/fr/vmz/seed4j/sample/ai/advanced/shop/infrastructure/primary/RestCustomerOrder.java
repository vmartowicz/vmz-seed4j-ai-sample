package fr.vmz.seed4j.sample.ai.advanced.shop.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.CustomerOrder;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.TrackingNumber;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestCustomerOrder(
  UUID id,
  String customerEmail,
  String status,
  List<RestOrderLine> lines,
  BigDecimal itemsTotal,
  BigDecimal discount,
  BigDecimal shipping,
  BigDecimal total,
  @Nullable String trackingNumber,
  @Nullable LocalDate deliveredOn,
  boolean returnRequested,
  @Nullable RestRefund refund,
  List<RestStatusChange> history
) {
  static RestCustomerOrder from(CustomerOrder order) {
    var pricing = order.pricing();

    return new RestCustomerOrder(
      order.id().get(),
      order.customer().get(),
      order.status().name(),
      order.lines().stream().map(RestOrderLine::from).toList(),
      pricing.items().get(),
      pricing.discount().get(),
      pricing.shipping().get(),
      pricing.total().get(),
      order.trackingNumber().map(TrackingNumber::get).orElse(null),
      order.deliveredOn().orElse(null),
      order.returnRequested(),
      order.refund().map(RestRefund::from).orElse(null),
      order.history().stream().map(RestStatusChange::from).toList()
    );
  }
}
