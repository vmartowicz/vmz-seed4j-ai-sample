package fr.vmz.seed4j.sample.ai.advanced.shop.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.TrackingNumber;
import org.jspecify.annotations.Nullable;

record RestShipment(@Nullable String trackingNumber) {
  TrackingNumber toDomain() {
    return new TrackingNumber(trackingNumber);
  }
}
