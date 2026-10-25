package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

import static fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.ScheduleFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class PlannerTest {

  private final CoachesRepository coaches = mock(CoachesRepository.class, CALLS_REAL_METHODS);
  private final GymClassesRepository classes = mock(GymClassesRepository.class);
  private final ClassSessionsRepository sessions = mock(ClassSessionsRepository.class, CALLS_REAL_METHODS);
  private final SessionCancellations cancellations = mock(SessionCancellations.class);
  private final Planner planner = new Planner(coaches, classes, sessions, cancellations);

  private final Coach alice = yogaCoach();
  private final Coach bob = yogaCoach();

  @BeforeEach
  void loadCoaches() {
    doReturn(Optional.of(alice)).when(coaches).get(alice.id());
    doReturn(Optional.of(bob)).when(coaches).get(bob.id());
    doReturn(List.of()).when(classes).all();
    doReturn(List.of()).when(sessions).ofWeek(any());
  }

  @Test
  void shouldRegisterCoach() {
    var coach = planner.register(new CoachToRegister("Bob Durand", Set.of(Discipline.BOXING)));

    verify(coaches).save(coach);
  }

  @Nested
  class Schedule {

    @Test
    void shouldScheduleClass() {
      var gymClass = planner.schedule(yogaOn(alice, DayOfWeek.MONDAY, "09:00"));

      verify(classes).save(gymClass);
    }

    @Test
    void shouldNotScheduleClassForUnknownCoach() {
      var unknown = CoachId.newId();
      doReturn(Optional.empty()).when(coaches).get(unknown);
      var gymClass = new ClassToSchedule(Discipline.YOGA, unknown, studioA(), DayOfWeek.MONDAY, LocalTime.NOON, 60, 10);

      assertThatThrownBy(() -> planner.schedule(gymClass)).isExactlyInstanceOf(UnknownCoachException.class);
    }

    @Test
    void shouldNotUseBusyStudio() {
      doReturn(List.of(yogaOn(bob, DayOfWeek.MONDAY, "09:30").schedule())).when(classes).all();

      assertThatThrownBy(() -> planner.schedule(yogaOn(alice, DayOfWeek.MONDAY, "09:00"))).isExactlyInstanceOf(
        StudioAlreadyUsedException.class
      );
      verify(classes, never()).save(any());
    }

    @Test
    void shouldUseStudioFreedOnOtherDaysOrInOtherStudios() {
      var otherStudio = new ClassToSchedule(Discipline.YOGA, bob.id(), Studios.studio(Gym.PART_DIEU, "B"), DayOfWeek.MONDAY, LocalTime.of(9, 0), 60, 20);
      doReturn(List.of(yogaOn(bob, DayOfWeek.TUESDAY, "09:00").schedule(), otherStudio.schedule())).when(classes).all();

      assertThatCode(() -> planner.schedule(yogaOn(alice, DayOfWeek.MONDAY, "09:00"))).doesNotThrowAnyException();
    }

    @Test
    void shouldCheckCoachAgendaAgainstHerClasses() {
      var elsewhere = new ClassToSchedule(Discipline.YOGA, alice.id(), Studios.studio(Gym.ECULLY, "A"), DayOfWeek.MONDAY, LocalTime.of(10, 0), 60, 20);
      var bobs = new ClassToSchedule(Discipline.YOGA, bob.id(), Studios.studio(Gym.ECULLY, "B"), DayOfWeek.MONDAY, LocalTime.of(11, 30), 60, 15);
      doReturn(List.of(elsewhere.schedule(), bobs.schedule())).when(classes).all();

      assertThatThrownBy(() -> planner.schedule(yogaOn(alice, DayOfWeek.MONDAY, "11:15"))).isExactlyInstanceOf(
        CoachNotAvailableException.class
      );
    }
  }

  @Nested
  class Publish {

    @Test
    void shouldCreateSessionsOfWeekInStartOrder() {
      var tuesday = yogaOn(alice, DayOfWeek.TUESDAY, "09:00").schedule();
      var monday = yogaOn(bob, DayOfWeek.MONDAY, "18:00").schedule();
      doReturn(List.of(tuesday, monday)).when(classes).all();

      var published = planner.publish(MONDAY);

      assertThat(published).extracting(ClassSession::gymClass).containsExactly(monday.id(), tuesday.id());
      verify(sessions, times(2)).save(any());
    }

    @Test
    void shouldPublishOnlyFromMonday() {
      assertThatThrownBy(() -> planner.publish(MONDAY.plusDays(1))).isExactlyInstanceOf(InvalidWeekException.class);
    }

    @Test
    void shouldNotPublishWeekTwice() {
      doReturn(List.of(yogaOn(alice, DayOfWeek.TUESDAY, "09:00").schedule().sessionIn(MONDAY))).when(sessions).ofWeek(MONDAY);

      assertThatThrownBy(() -> planner.publish(MONDAY)).isExactlyInstanceOf(WeekAlreadyPublishedException.class);
    }

    @Test
    void shouldListSessionsOfWeekInStartOrder() {
      var late = yogaOn(alice, DayOfWeek.FRIDAY, "09:00").schedule().sessionIn(MONDAY);
      var early = yogaOn(alice, DayOfWeek.MONDAY, "09:00").schedule().sessionIn(MONDAY);
      doReturn(List.of(late, early)).when(sessions).ofWeek(MONDAY);

      assertThat(planner.weekSessions(MONDAY)).containsExactly(early, late);
    }
  }

  @Nested
  class Sessions {

    @Test
    void shouldReplaceCoachOfSession() {
      var session = known(yogaOn(alice, DayOfWeek.MONDAY, "09:00").schedule().sessionIn(MONDAY));
      doReturn(List.of(session)).when(sessions).ofWeek(MONDAY);

      planner.replaceCoach(session.id(), bob.id());

      assertThat(session.coach()).isEqualTo(bob.id());
      verify(sessions).save(session);
    }

    @Test
    void shouldNotReplaceWithBusyCoach() {
      var session = known(yogaOn(alice, DayOfWeek.MONDAY, "09:00").schedule().sessionIn(MONDAY));
      var bobsSession = new ClassToSchedule(Discipline.YOGA, bob.id(), Studios.studio(Gym.ECULLY, "A"), DayOfWeek.MONDAY, LocalTime.of(8, 0), 45, 20)
        .schedule()
        .sessionIn(MONDAY);
      doReturn(List.of(session, bobsSession)).when(sessions).ofWeek(MONDAY);

      assertThatThrownBy(() -> planner.replaceCoach(session.id(), bob.id())).isExactlyInstanceOf(CoachNotAvailableException.class);
      assertThat(session.coach()).isEqualTo(alice.id());
    }

    @Test
    void shouldIgnoreCancelledSessionsInCoachAgenda() {
      var session = known(yogaOn(alice, DayOfWeek.MONDAY, "09:00").schedule().sessionIn(MONDAY));
      var cancelled = new ClassToSchedule(Discipline.YOGA, bob.id(), Studios.studio(Gym.ECULLY, "A"), DayOfWeek.MONDAY, LocalTime.of(8, 0), 45, 20)
        .schedule()
        .sessionIn(MONDAY);
      cancelled.cancel();
      doReturn(List.of(session, cancelled)).when(sessions).ofWeek(MONDAY);

      planner.replaceCoach(session.id(), bob.id());

      assertThat(session.coach()).isEqualTo(bob.id());
    }

    @Test
    void shouldCancelSessionAndTellBookings() {
      var session = known(yogaOn(alice, DayOfWeek.MONDAY, "09:00").schedule().sessionIn(MONDAY));

      planner.cancel(session.id());

      assertThat(session.isCancelled()).isTrue();
      verify(sessions).save(session);
      verify(cancellations).cancelled(session.id());
    }

    @Test
    void shouldNotFindUnknownSession() {
      var unknown = SessionId.newId();
      doReturn(Optional.empty()).when(sessions).get(unknown);

      assertThatThrownBy(() -> planner.replaceCoach(unknown, bob.id())).isExactlyInstanceOf(UnknownSessionException.class);
    }
  }

  private ClassSession known(ClassSession session) {
    doReturn(Optional.of(session)).when(sessions).get(session.id());

    return session;
  }
}
