package fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record EmployeeName(String name) {
  public EmployeeName {
    Assert.field("name", name).notBlank().maxLength(100);
  }

  public String get() {
    return name();
  }
}
