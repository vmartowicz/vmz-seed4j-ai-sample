package fr.vmz.seed4j.sample.ai.intermediate.clinic.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.List;

public record ClinicPage<T>(List<T> content, int page, int totalElements) {
  public ClinicPage {
    Assert.notNull("content", content);

    content = List.copyOf(content);
  }

  public static <T> ClinicPage<T> of(List<T> elements, Pagination pagination) {
    Assert.notNull("elements", elements);
    Assert.notNull("pagination", pagination);

    var content = elements.stream().skip(pagination.offset()).limit(Pagination.PAGE_SIZE).toList();

    return new ClinicPage<>(content, pagination.page(), elements.size());
  }

  public int pageSize() {
    return Pagination.PAGE_SIZE;
  }

  public int totalPages() {
    return Math.ceilDiv(totalElements, Pagination.PAGE_SIZE);
  }
}
