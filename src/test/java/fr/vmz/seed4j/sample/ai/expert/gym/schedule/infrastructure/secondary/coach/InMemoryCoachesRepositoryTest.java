package fr.vmz.seed4j.sample.ai.expert.gym.schedule.infrastructure.secondary.coach;

import static fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.ScheduleFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.CoachId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryCoachesRepositoryTest {

  private final InMemoryCoachesRepository coaches = new InMemoryCoachesRepository();

  @Test
  void shouldGetSavedCoach() {
    var coach = yogaCoach();

    coaches.save(coach);

    assertThat(coaches.get(coach.id())).containsSame(coach);
    assertThat(coaches.get(CoachId.newId())).isEmpty();
  }
}
