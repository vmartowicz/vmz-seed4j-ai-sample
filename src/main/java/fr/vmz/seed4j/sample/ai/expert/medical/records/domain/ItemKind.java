package fr.vmz.seed4j.sample.ai.expert.medical.records.domain;

import java.time.Period;

public enum ItemKind {
  MEDICINE(Period.ofMonths(3)),
  LAB_TEST(Period.ofYears(1)),
  IMAGING(Period.ofYears(1));

  private final Period validity;

  ItemKind(Period validity) {
    this.validity = validity;
  }

  Period validity() {
    return validity;
  }
}
