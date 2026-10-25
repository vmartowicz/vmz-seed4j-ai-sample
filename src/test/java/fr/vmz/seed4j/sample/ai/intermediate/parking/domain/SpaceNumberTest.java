package fr.vmz.seed4j.sample.ai.intermediate.parking.domain;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.NumberValueTooHighException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.NumberValueTooLowException;
import org.junit.jupiter.api.Test;

@UnitTest
class SpaceNumberTest {

  @Test
  void shouldNotBuildSpace0() {
    assertThatThrownBy(() -> new SpaceNumber(0)).isExactlyInstanceOf(NumberValueTooLowException.class);
  }

  @Test
  void shouldNotBuildSpace11() {
    assertThatThrownBy(() -> new SpaceNumber(11)).isExactlyInstanceOf(NumberValueTooHighException.class);
  }

  @Test
  void shouldBuildSpaces1To10() {
    assertThat(new SpaceNumber(1).get()).isEqualTo(1);
    assertThat(new SpaceNumber(10).get()).isEqualTo(10);
  }

  @Test
  void shouldHave10VisitorSpaces() {
    assertThat(SpaceNumber.all()).extracting(SpaceNumber::get).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
  }
}
