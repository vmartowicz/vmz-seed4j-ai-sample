package fr.vmz.seed4j.sample.ai.advanced.leave.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigDecimal;

public record LeaveDays(BigDecimal days) {
  public static final LeaveDays ZERO = new LeaveDays(BigDecimal.ZERO);
  public static final LeaveDays YEARLY_ENTITLEMENT = new LeaveDays(BigDecimal.valueOf(25));

  public LeaveDays {
    Assert.field("days", days).min(0);

    if (days.multiply(BigDecimal.TWO).stripTrailingZeros().scale() > 0) {
      throw new InvalidLeaveDaysException(days);
    }

    days = days.setScale(1);
  }

  public static LeaveDays ofHalfDays(int halfDays) {
    return new LeaveDays(BigDecimal.valueOf(halfDays).divide(BigDecimal.TWO));
  }

  public LeaveDays plus(LeaveDays other) {
    return new LeaveDays(days.add(other.days()));
  }

  public LeaveDays minus(LeaveDays other) {
    return new LeaveDays(days.subtract(other.days()));
  }

  public boolean covers(LeaveDays other) {
    return days.compareTo(other.days()) >= 0;
  }

  public BigDecimal get() {
    return days();
  }
}
