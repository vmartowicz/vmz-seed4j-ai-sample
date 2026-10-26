package fr.vmz.seed4j.sample.ai.expert.medical.records.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record PrescriptionItem(ItemKind kind, String code) {
  public PrescriptionItem {
    Assert.notNull("kind", kind);
    Assert.notBlank("code", code);
  }

  boolean is(ItemKind otherKind, String otherCode) {
    return kind == otherKind && code.equalsIgnoreCase(otherCode);
  }
}
