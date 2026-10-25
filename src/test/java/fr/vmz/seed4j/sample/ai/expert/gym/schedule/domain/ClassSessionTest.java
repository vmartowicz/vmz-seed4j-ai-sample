package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

import static fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.ScheduleFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

@UnitTest
class ClassSessionTest {

  private final Coach coach = yogaCoach();
  private final GymClass gymClass = yogaOn(coach, DayOfWeek.TUESDAY, "07:00").schedule();

  @Test
  void shouldCopyClassInWeek() {
    var session = gymClass.sessionIn(MONDAY);

    assertThat(session.id()).isNotNull();
    assertThat(session.gymClass()).isEqualTo(gymClass.id());
    assertThat(session.discipline()).isEqualTo(Discipline.YOGA);
    assertThat(session.coach()).isEqualTo(coach.id());
    assertThat(session.studio()).isEqualTo(studioA());
    assertThat(session.start()).isEqualTo(LocalDateTime.of(2026, 3, 10, 7, 0));
    assertThat(session.end()).isEqualTo(LocalDateTime.of(2026, 3, 10, 8, 0));
    assertThat(session.capacity()).isEqualTo(20);
    assertThat(session.isCancelled()).isFalse();
    assertThat(session.slot().gym()).isEqualTo(studioA().gym());
  }

  @Test
  void shouldReplaceCoach() {
    var session = gymClass.sessionIn(MONDAY);
    var replacement = yogaCoach();

    session.replaceCoach(replacement.id());

    assertThat(session.coach()).isEqualTo(replacement.id());
  }

  @Test
  void shouldCancelSession() {
    var session = gymClass.sessionIn(MONDAY);

    session.cancel();

    assertThat(session.isCancelled()).isTrue();
  }

  @Test
  void shouldNotChangeCancelledSession() {
    var session = gymClass.sessionIn(MONDAY);
    session.cancel();

    assertThatThrownBy(session::cancel).isExactlyInstanceOf(SessionCancelledException.class);
    assertThatThrownBy(() -> session.replaceCoach(coach.id())).isExactlyInstanceOf(SessionCancelledException.class);
  }
}
