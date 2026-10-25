package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record RejectionComment(String comment) {
  public RejectionComment {
    Assert.field("comment", comment).notBlank().maxLength(255);
  }

  public String get() {
    return comment();
  }
}
