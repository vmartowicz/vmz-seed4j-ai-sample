package fr.vmz.seed4j.sample.ai.clinic.domain.owner;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.StringTooLongException;
import org.junit.jupiter.api.Test;

@UnitTest
class PetNameTest {

  @Test
  void shouldNotBuildWithoutName() {
    assertThatThrownBy(() -> new PetName(" ")).isExactlyInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldNotBuildWithMoreThan30Characters() {
    assertThatThrownBy(() -> new PetName("a".repeat(31))).isExactlyInstanceOf(StringTooLongException.class);
  }

  @Test
  void shouldIgnoreLeadingAndTrailingSpaces() {
    assertThat(new PetName("  " + "a".repeat(30) + " ").get()).isEqualTo("a".repeat(30));
  }

  @Test
  void shouldBeSameNameIgnoringCase() {
    assertThat(new PetName("Rex").sameAs(new PetName("rex"))).isTrue();
  }

  @Test
  void shouldNotBeSameAsAnotherName() {
    assertThat(new PetName("Rex").sameAs(new PetName("Rox"))).isFalse();
  }
}
