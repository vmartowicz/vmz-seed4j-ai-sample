package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.StringTooLongException;
import org.junit.jupiter.api.Test;

@UnitTest
class RoomNameTest {

  @Test
  void shouldNotBuildWithoutName() {
    assertThatThrownBy(() -> new RoomName(" ")).isExactlyInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldNotBuildWithMoreThan50Characters() {
    assertThatThrownBy(() -> new RoomName("a".repeat(51))).isExactlyInstanceOf(StringTooLongException.class);
  }

  @Test
  void shouldBuildTrimmedNameWith50Characters() {
    assertThat(new RoomName(" " + "a".repeat(50) + " ").get()).isEqualTo("a".repeat(50));
  }

  @Test
  void shouldBeSameNameIgnoringCase() {
    assertThat(new RoomName("Everest").sameAs(new RoomName("EVEREST"))).isTrue();
  }

  @Test
  void shouldNotBeSameAsAnotherName() {
    assertThat(new RoomName("Everest").sameAs(new RoomName("Kilimanjaro"))).isFalse();
  }
}
