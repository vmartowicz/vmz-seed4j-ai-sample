package fr.vmz.seed4j.sample.ai.advanced.shop.domain.article;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record Stock(int units) {
  private static final int ALERT_THRESHOLD = 5;

  public Stock {
    Assert.field("stock", units).min(0);
  }

  public boolean covers(int requested) {
    return units >= requested;
  }

  public boolean isLow() {
    return units < ALERT_THRESHOLD;
  }

  public Stock minus(int removed) {
    return new Stock(units - removed);
  }

  public Stock plus(int added) {
    return new Stock(units + added);
  }

  public int get() {
    return units();
  }
}
