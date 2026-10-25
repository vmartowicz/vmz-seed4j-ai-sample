package fr.vmz.seed4j.sample.ai.expert.gym.schedule.infrastructure.secondary.session;

import static fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.ScheduleFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.SessionId;
import java.time.DayOfWeek;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryClassSessionsRepositoryTest {

  private final InMemoryClassSessionsRepository sessions = new InMemoryClassSessionsRepository();

  @Test
  void shouldFindSessionsOfWeek() {
    var gymClass = yogaOn(yogaCoach(), DayOfWeek.SUNDAY, "20:00").schedule();
    var thisWeek = gymClass.sessionIn(MONDAY);
    var nextWeek = gymClass.sessionIn(MONDAY.plusWeeks(1));
    sessions.save(gymClass.sessionIn(MONDAY.minusWeeks(1)));
    sessions.save(thisWeek);
    sessions.save(nextWeek);

    assertThat(sessions.get(thisWeek.id())).containsSame(thisWeek);
    assertThat(sessions.get(SessionId.newId())).isEmpty();
    assertThat(sessions.ofWeek(MONDAY)).containsExactly(thisWeek);
  }
}
