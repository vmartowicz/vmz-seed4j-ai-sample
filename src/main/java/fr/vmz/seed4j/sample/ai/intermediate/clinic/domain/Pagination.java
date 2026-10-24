package fr.vmz.seed4j.sample.ai.intermediate.clinic.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record Pagination(int page) {
  public static final int PAGE_SIZE = 5;

  public Pagination {
    Assert.field("page", page).min(1);
  }

  public long offset() {
    return (long) (page - 1) * PAGE_SIZE;
  }
}
