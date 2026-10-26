package fr.vmz.seed4j.sample.ai.shared.rental.domain;

import java.util.List;

public enum Category {
  ECO,
  COMFORT,
  SUV,
  VAN,
  PREMIUM;

  private static final List<Category> UPGRADE_LADDER = List.of(ECO, COMFORT, SUV, PREMIUM);

  public List<Category> upgrades() {
    var rung = UPGRADE_LADDER.indexOf(this);
    if (rung < 0) {
      return List.of();
    }

    return UPGRADE_LADDER.subList(rung + 1, UPGRADE_LADDER.size());
  }

  public boolean isForbiddenToYoungDrivers() {
    return this == PREMIUM || this == VAN;
  }
}
