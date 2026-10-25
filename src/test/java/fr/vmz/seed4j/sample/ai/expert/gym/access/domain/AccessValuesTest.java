package fr.vmz.seed4j.sample.ai.expert.gym.access.domain;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

@UnitTest
class AccessValuesTest {

  private static final LocalDateTime NOW = LocalDateTime.of(2026, 3, 10, 18, 0);

  @Test
  void shouldGenerateBadgeNumbers() {
    var badge = BadgeNumber.generate();

    assertThat(badge.get()).matches("FC-[0-9A-F]{8}");
    assertThat(badge).isNotEqualTo(BadgeNumber.generate());
  }

  @Test
  void shouldNormalizeBadgeNumber() {
    assertThat(new BadgeNumber(" fc-0a1b2c3d ").get()).isEqualTo("FC-0A1B2C3D");
  }

  @Test
  void shouldNotBuildBlankBadgeNumber() {
    assertThatThrownBy(() -> new BadgeNumber(" ")).isInstanceOf(AssertionException.class);
  }

  @Test
  void shouldLimitPartDieuTo150People() {
    assertThat(GymCapacity.of(Gym.PART_DIEU)).isEqualTo(150);
    assertThat(GymCapacity.of(Gym.ECULLY)).isEqualTo(100);
  }

  @Test
  void shouldExplainRefusals() {
    assertThat(RefusalReason.IN_ARREARS.message()).contains("front desk");
    assertThat(Entry.refused(RefusalReason.GYM_FULL).refusal()).contains(RefusalReason.GYM_FULL);
    assertThat(Entry.welcome().accepted()).isTrue();
    assertThat(Entry.welcome().refusal()).isEmpty();
  }

  @Test
  void shouldTrackPresence() {
    var presence = new Presence(MemberId.newId(), Gym.ECULLY, NOW);

    assertThat(presence.isOpen()).isTrue();
    assertThat(presence.isExpiredAt(NOW.plusHours(4).minusMinutes(1))).isFalse();
    assertThat(presence.isExpiredAt(NOW.plusHours(4))).isTrue();

    presence.exit(NOW.plusHours(1));

    assertThat(presence.isOpen()).isFalse();
    assertThat(presence.exitedAt()).contains(NOW.plusHours(1));
  }
}
