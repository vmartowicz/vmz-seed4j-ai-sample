package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record TrackingNumber(String number) {
  public TrackingNumber {
    Assert.field("trackingNumber", number).notBlank().maxLength(50);
  }

  public String get() {
    return number();
  }
}
