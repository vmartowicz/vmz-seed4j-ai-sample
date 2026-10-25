package fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record TeamName(String team) {
  public TeamName {
    Assert.field("team", team).notBlank().maxLength(50);
  }

  public String get() {
    return team();
  }
}
