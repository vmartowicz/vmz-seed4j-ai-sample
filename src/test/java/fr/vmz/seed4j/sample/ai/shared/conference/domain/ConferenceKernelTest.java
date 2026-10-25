package fr.vmz.seed4j.sample.ai.shared.conference.domain;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

@UnitTest
class ConferenceKernelTest {

  @Test
  void shouldNormalizeEmail() {
    assertThat(new Email(" Jane@Mail.COM ").get()).isEqualTo("jane@mail.com");
  }

  @Test
  void shouldNotBuildInvalidEmail() {
    assertThatThrownBy(() -> new Email("jane@mail")).isExactlyInstanceOf(InvalidEmailException.class).hasMessageContaining("jane@mail");
  }

  @Test
  void shouldNotBuildBlankEmail() {
    assertThatThrownBy(() -> new Email(" ")).isInstanceOf(AssertionException.class);
  }

  @Test
  void shouldHave5RoomsWithTheirCapacity() {
    assertThat(Room.AMPHI.capacity()).isEqualTo(500);
    assertThat(Room.CROIX_ROUSSE.capacity()).isEqualTo(40);
    assertThat(Room.values()).hasSize(5);
  }

  @Test
  void shouldTakePlaceOn2DaysInOctober() {
    assertThat(Edition.days()).containsExactly(LocalDate.of(2026, 10, 15), LocalDate.of(2026, 10, 16));
  }

  @Test
  void shouldOpenCallForPapersFromFebruaryToMarch() {
    assertThat(Edition.isCallForPapersOpenOn(LocalDate.of(2026, 1, 31))).isFalse();
    assertThat(Edition.isCallForPapersOpenOn(LocalDate.of(2026, 2, 1))).isTrue();
    assertThat(Edition.isCallForPapersOpenOn(LocalDate.of(2026, 3, 31))).isTrue();
    assertThat(Edition.isCallForPapersOpenOn(LocalDate.of(2026, 4, 1))).isFalse();
  }
}
