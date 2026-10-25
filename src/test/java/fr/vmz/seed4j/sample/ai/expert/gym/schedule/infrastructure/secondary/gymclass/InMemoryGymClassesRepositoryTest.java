package fr.vmz.seed4j.sample.ai.expert.gym.schedule.infrastructure.secondary.gymclass;

import static fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.ScheduleFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.time.DayOfWeek;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryGymClassesRepositoryTest {

  private final InMemoryGymClassesRepository classes = new InMemoryGymClassesRepository();

  @Test
  void shouldListSavedClasses() {
    var gymClass = yogaOn(yogaCoach(), DayOfWeek.MONDAY, "09:00").schedule();

    classes.save(gymClass);

    assertThat(classes.all()).containsExactly(gymClass);
  }
}
