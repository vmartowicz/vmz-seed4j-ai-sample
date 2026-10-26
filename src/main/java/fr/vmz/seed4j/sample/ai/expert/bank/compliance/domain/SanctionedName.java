package fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.Locale;

public record SanctionedName(String name) {
  public SanctionedName {
    Assert.field("name", name).notBlank().maxLength(200);

    name = name.trim();
  }

  boolean matches(String payee) {
    return name.toLowerCase(Locale.ROOT).equals(payee.trim().toLowerCase(Locale.ROOT));
  }

  public String get() {
    return name();
  }
}
