package fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record EmployeeEmail(String email) {
  public EmployeeEmail {
    Assert.notBlank("email", email);
  }

  public String get() {
    return email();
  }
}
