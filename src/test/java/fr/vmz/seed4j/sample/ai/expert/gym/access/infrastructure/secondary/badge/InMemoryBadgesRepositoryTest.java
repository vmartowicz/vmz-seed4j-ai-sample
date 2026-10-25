package fr.vmz.seed4j.sample.ai.expert.gym.access.infrastructure.secondary.badge;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.Badge;
import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.BadgeNumber;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryBadgesRepositoryTest {

  private final InMemoryBadgesRepository badges = new InMemoryBadgesRepository();

  @Test
  void shouldGetSavedBadge() {
    var badge = new Badge(BadgeNumber.generate(), MemberId.newId());

    badges.save(badge);

    assertThat(badges.get(badge.number())).contains(badge);
    assertThat(badges.get(BadgeNumber.generate())).isEmpty();
  }
}
