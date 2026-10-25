package fr.vmz.seed4j.sample.ai.expert.conference.program.infrastructure.secondary.session;

import static fr.vmz.seed4j.sample.ai.expert.conference.program.domain.ProgramFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.conference.program.domain.ProgramSession;
import fr.vmz.seed4j.sample.ai.expert.conference.program.domain.Track;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Room;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryProgramSessionsRepositoryTest {

  private final InMemoryProgramSessionsRepository sessions = new InMemoryProgramSessionsRepository();

  @Test
  void shouldSaveAndDeleteSessions() {
    var session = new ProgramSession(talk(Track.BACK, "jane"), Room.AMPHI, List.of(dayTwo(2)));

    sessions.save(session);

    assertThat(sessions.get(session.talk().talk())).contains(session);
    assertThat(sessions.get(UUID.randomUUID())).isEmpty();
    assertThat(sessions.all()).containsExactly(session);

    sessions.delete(session.talk().talk());

    assertThat(sessions.all()).isEmpty();
  }
}
