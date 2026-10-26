package fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain;

import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import java.math.BigDecimal;

public final class DailyRates {

  private DailyRates() {}

  public static BigDecimal of(Category category) {
    return switch (category) {
      case ECO -> new BigDecimal("35");
      case COMFORT -> new BigDecimal("49");
      case SUV -> new BigDecimal("69");
      case VAN -> new BigDecimal("79");
      case PREMIUM -> new BigDecimal("119");
    };
  }
}
