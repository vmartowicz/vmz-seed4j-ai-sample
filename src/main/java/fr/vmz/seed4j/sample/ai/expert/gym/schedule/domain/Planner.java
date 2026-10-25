package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;

public class Planner {

  private static final LocalDate REFERENCE_WEEK = LocalDate.of(2001, 1, 1);
  private static final Comparator<ClassSession> BY_START = Comparator.comparing(ClassSession::start);

  private final CoachesRepository coaches;
  private final GymClassesRepository classes;
  private final ClassSessionsRepository sessions;
  private final SessionCancellations cancellations;

  public Planner(CoachesRepository coaches, GymClassesRepository classes, ClassSessionsRepository sessions, SessionCancellations cancellations) {
    this.coaches = coaches;
    this.classes = classes;
    this.sessions = sessions;
    this.cancellations = cancellations;
  }

  public Coach register(CoachToRegister coach) {
    var registered = coach.register();

    coaches.save(registered);

    return registered;
  }

  public GymClass schedule(ClassToSchedule toSchedule) {
    Assert.notNull("class", toSchedule);

    var coach = coaches.coach(toSchedule.coach());
    var gymClass = toSchedule.schedule();
    var slot = gymClass.slotIn(REFERENCE_WEEK);
    var existing = classes.all();

    if (existing.stream().anyMatch(other -> other.studio().equals(gymClass.studio()) && other.slotIn(REFERENCE_WEEK).overlaps(slot))) {
      throw new StudioAlreadyUsedException(gymClass.studio(), slot);
    }

    var taught = existing
      .stream()
      .filter(other -> other.coach().equals(coach.id()))
      .map(other -> other.slotIn(REFERENCE_WEEK))
      .toList();
    CoachAgenda.assertCanTeach(coach, gymClass.discipline(), slot, taught);

    classes.save(gymClass);
    return gymClass;
  }

  public List<ClassSession> publish(LocalDate monday) {
    if (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
      throw new InvalidWeekException(monday);
    }

    if (!sessions.ofWeek(monday).isEmpty()) {
      throw new WeekAlreadyPublishedException(monday);
    }

    var published = classes
      .all()
      .stream()
      .map(gymClass -> gymClass.sessionIn(monday))
      .sorted(BY_START)
      .toList();
    published.forEach(sessions::save);

    return published;
  }

  public List<ClassSession> weekSessions(LocalDate monday) {
    return sessions.ofWeek(monday).stream().sorted(BY_START).toList();
  }

  public ClassSession replaceCoach(SessionId id, CoachId replacement) {
    var session = sessions.session(id);
    var coach = coaches.coach(replacement);

    var monday = session.start().toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    var taught = sessions
      .ofWeek(monday)
      .stream()
      .filter(other -> !other.isCancelled())
      .filter(other -> other.coach().equals(coach.id()))
      .map(ClassSession::slot)
      .toList();
    CoachAgenda.assertCanTeach(coach, session.discipline(), session.slot(), taught);

    session.replaceCoach(coach.id());
    sessions.save(session);
    return session;
  }

  public ClassSession cancel(SessionId id) {
    var session = sessions.session(id);

    session.cancel();
    sessions.save(session);
    cancellations.cancelled(session.id());

    return session;
  }
}
