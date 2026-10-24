package fr.vmz.seed4j.sample.ai.clinic.domain.owner;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.StringTooLongException;
import org.junit.jupiter.api.Test;

@UnitTest
class VisitDescriptionTest {

  @Test
  void shouldNotBuildWithoutVisitDescription() {
    assertThatThrownBy(() -> new VisitDescription(" ")).isExactlyInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldNotBuildWithMoreThan255Characters() {
    assertThatThrownBy(() -> new VisitDescription("a".repeat(256))).isExactlyInstanceOf(StringTooLongException.class);
  }

  @Test
  void shouldBuildWith255Characters() {
    assertThat(new VisitDescription("a".repeat(255)).get()).isEqualTo("a".repeat(255));
  }
}
