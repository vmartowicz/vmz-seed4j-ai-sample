package fr.vmz.seed4j.sample.ai.intermediate.clinic.domain;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.NumberValueTooLowException;
import org.junit.jupiter.api.Test;

@UnitTest
class PaginationTest {

  @Test
  void shouldNotBuildPageZero() {
    assertThatThrownBy(() -> new Pagination(0)).isExactlyInstanceOf(NumberValueTooLowException.class);
  }

  @Test
  void shouldStartFirstPageAtFirstElement() {
    assertThat(new Pagination(1).offset()).isZero();
  }

  @Test
  void shouldSkipPreviousPagesOf5Elements() {
    assertThat(new Pagination(3).offset()).isEqualTo(10);
  }
}
