package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain;

import java.math.BigDecimal;
import org.jspecify.annotations.Nullable;

public enum LabAnalysis {
  GLUCOSE(true, "0.70", "1.10", "0.50", null),
  LIPID_PROFILE(true, "0", "2.00", null, null),
  POTASSIUM(false, "3.5", "5.0", null, "6"),
  CREATININE(false, "6", "12", null, null);

  private final boolean fasting;
  private final BigDecimal low;
  private final BigDecimal high;
  private final @Nullable BigDecimal criticalBelow;
  private final @Nullable BigDecimal criticalAbove;

  LabAnalysis(boolean fasting, String low, String high, @Nullable String criticalBelow, @Nullable String criticalAbove) {
    this.fasting = fasting;
    this.low = new BigDecimal(low);
    this.high = new BigDecimal(high);
    this.criticalBelow = criticalBelow == null ? null : new BigDecimal(criticalBelow);
    this.criticalAbove = criticalAbove == null ? null : new BigDecimal(criticalAbove);
  }

  public boolean fasting() {
    return fasting;
  }

  public boolean isAbnormal(BigDecimal value) {
    return value.compareTo(low) < 0 || value.compareTo(high) > 0;
  }

  public boolean isCritical(BigDecimal value) {
    return (criticalBelow != null && value.compareTo(criticalBelow) < 0) || (criticalAbove != null && value.compareTo(criticalAbove) > 0);
  }
}
