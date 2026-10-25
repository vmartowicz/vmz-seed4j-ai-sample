package fr.vmz.seed4j.sample.ai.expert.gym.access.infrastructure.secondary.presence;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.Presence;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryPresencesRepositoryTest {

  private static final LocalDateTime NOW = LocalDateTime.of(2026, 3, 10, 18, 0);

  private final InMemoryPresencesRepository presences = new InMemoryPresencesRepository();

  @Test
  void shouldFindOpenPresences() {
    var member = MemberId.newId();
    var closed = new Presence(member, Gym.ECULLY, NOW.minusDays(1));
    closed.exit(NOW.minusDays(1).plusHours(1));
    var open = new Presence(member, Gym.ECULLY, NOW);
    var elsewhere = new Presence(MemberId.newId(), Gym.PART_DIEU, NOW);
    presences.save(closed);
    presences.save(open);
    presences.save(elsewhere);

    assertThat(presences.openOf(member)).containsSame(open);
    assertThat(presences.openOf(MemberId.newId())).isEmpty();
    assertThat(presences.openIn(Gym.ECULLY)).containsExactly(open);
  }

  @Test
  void shouldNotDuplicateSavedPresence() {
    var presence = new Presence(MemberId.newId(), Gym.ECULLY, NOW);
    presences.save(presence);
    presence.exit(NOW.plusHours(1));

    presences.save(presence);

    assertThat(presences.openIn(Gym.ECULLY)).isEmpty();
  }
}
