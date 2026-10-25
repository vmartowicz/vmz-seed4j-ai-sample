package fr.vmz.seed4j.sample.ai.expert.conference.onsite.infrastructure.secondary.presence;

import static fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.OnsiteFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.BadgeId;
import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.RoomPresence;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Room;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryRoomPresencesRepositoryTest {

  private final InMemoryRoomPresencesRepository presences = new InMemoryRoomPresencesRepository();

  @Test
  void shouldFindOpenPresences() {
    var badge = BadgeId.newId();
    var closed = new RoomPresence(badge, Room.AMPHI, NOW.minusHours(1), null);
    closed.exit(NOW);
    var open = new RoomPresence(badge, Room.SAONE, NOW, null);
    var other = new RoomPresence(BadgeId.newId(), Room.AMPHI, NOW, null);
    presences.save(closed);
    presences.save(open);
    presences.save(other);

    assertThat(presences.openOf(badge)).containsSame(open);
    assertThat(presences.openOf(BadgeId.newId())).isEmpty();
    assertThat(presences.openIn(Room.AMPHI)).containsExactly(other);
    assertThat(presences.all()).hasSize(3);
  }
}
