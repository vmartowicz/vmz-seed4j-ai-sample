package fr.vmz.seed4j.sample.ai.intermediate.clinic.domain;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

@UnitTest
class ClinicPageTest {

  private static final List<Integer> TWELVE_ELEMENTS = IntStream.rangeClosed(1, 12).boxed().toList();

  @Test
  void shouldPaginateBy5() {
    var page = ClinicPage.of(TWELVE_ELEMENTS, new Pagination(2));

    assertThat(page.content()).containsExactly(6, 7, 8, 9, 10);
    assertThat(page.page()).isEqualTo(2);
    assertThat(page.pageSize()).isEqualTo(5);
    assertThat(page.totalElements()).isEqualTo(12);
    assertThat(page.totalPages()).isEqualTo(3);
  }

  @Test
  void shouldGetRemainingElementsOnLastPage() {
    assertThat(ClinicPage.of(TWELVE_ELEMENTS, new Pagination(3)).content()).containsExactly(11, 12);
  }

  @Test
  void shouldGetEmptyPageWithoutElements() {
    var page = ClinicPage.of(List.of(), new Pagination(1));

    assertThat(page.content()).isEmpty();
    assertThat(page.totalElements()).isZero();
    assertThat(page.totalPages()).isZero();
  }
}
