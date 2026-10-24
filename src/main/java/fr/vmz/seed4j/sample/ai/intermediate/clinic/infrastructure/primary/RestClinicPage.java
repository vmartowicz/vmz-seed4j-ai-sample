package fr.vmz.seed4j.sample.ai.intermediate.clinic.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.ClinicPage;
import java.util.List;
import java.util.function.Function;

record RestClinicPage<T>(List<T> content, int page, int pageSize, int totalElements, int totalPages) {
  static <D, T> RestClinicPage<T> from(ClinicPage<D> page, Function<D, T> mapper) {
    return new RestClinicPage<>(
      page.content().stream().map(mapper).toList(),
      page.page(),
      page.pageSize(),
      page.totalElements(),
      page.totalPages()
    );
  }
}
