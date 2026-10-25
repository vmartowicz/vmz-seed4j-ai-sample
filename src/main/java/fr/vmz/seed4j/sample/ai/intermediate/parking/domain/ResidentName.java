package fr.vmz.seed4j.sample.ai.intermediate.parking.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.Locale;

public record ResidentName(String name) {
  public ResidentName {
    Assert.notBlank("name", name);

    name = name.trim();
    Assert.field("name", name).maxLength(100);
  }

  public boolean sameAs(ResidentName other) {
    return normalized().equals(other.normalized());
  }

  private String normalized() {
    return name.replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
  }

  public String get() {
    return name();
  }
}
