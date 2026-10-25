package fr.vmz.seed4j.sample.ai.advanced.shop.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.StatusChange;
import java.time.LocalDateTime;

record RestStatusChange(String status, LocalDateTime at) {
  static RestStatusChange from(StatusChange change) {
    return new RestStatusChange(change.status().name(), change.at());
  }
}
