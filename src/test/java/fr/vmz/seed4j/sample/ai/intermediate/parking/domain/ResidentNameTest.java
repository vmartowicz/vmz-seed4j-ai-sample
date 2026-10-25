package fr.vmz.seed4j.sample.ai.intermediate.parking.domain;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.StringTooLongException;
import org.junit.jupiter.api.Test;

@UnitTest
class ResidentNameTest {

  @Test
  void shouldNotBuildWithoutName() {
    assertThatThrownBy(() -> new ResidentName(" ")).isExactlyInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldNotBuildWithMoreThan100Characters() {
    assertThatThrownBy(() -> new ResidentName("a".repeat(101))).isExactlyInstanceOf(StringTooLongException.class);
  }

  @Test
  void shouldKeepTrimmedName() {
    assertThat(new ResidentName("  Jean Dupont ").get()).isEqualTo("Jean Dupont");
  }

  @Test
  void shouldBeSameResidentIgnoringCaseAndSpaces() {
    assertThat(new ResidentName("Jean Dupont").sameAs(new ResidentName(" jean   DUPONT "))).isTrue();
  }

  @Test
  void shouldNotBeSameAsAnotherResident() {
    assertThat(new ResidentName("Jean Dupont").sameAs(new ResidentName("Jeanne Dupont"))).isFalse();
  }
}
