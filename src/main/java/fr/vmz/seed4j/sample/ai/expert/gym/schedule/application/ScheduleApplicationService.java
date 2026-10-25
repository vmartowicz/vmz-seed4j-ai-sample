package fr.vmz.seed4j.sample.ai.expert.gym.schedule.application;

import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.ClassSession;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.ClassSessionsRepository;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.ClassToSchedule;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.Coach;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.CoachId;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.CoachToRegister;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.CoachesRepository;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.GymClass;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.GymClassesRepository;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.Planner;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.SessionCancellations;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.SessionId;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ScheduleApplicationService {

  private final ClassSessionsRepository sessions;
  private final Planner planner;

  public ScheduleApplicationService(
    CoachesRepository coaches,
    GymClassesRepository classes,
    ClassSessionsRepository sessions,
    SessionCancellations cancellations
  ) {
    this.sessions = sessions;
    planner = new Planner(coaches, classes, sessions, cancellations);
  }

  public Coach register(CoachToRegister coach) {
    return planner.register(coach);
  }

  public GymClass schedule(ClassToSchedule gymClass) {
    return planner.schedule(gymClass);
  }

  public List<ClassSession> publish(LocalDate monday) {
    return planner.publish(monday);
  }

  public List<ClassSession> weekSessions(LocalDate monday) {
    return planner.weekSessions(monday);
  }

  public Optional<ClassSession> findSession(SessionId session) {
    return sessions.get(session);
  }

  public ClassSession replaceCoach(SessionId session, CoachId coach) {
    return planner.replaceCoach(session, coach);
  }

  public ClassSession cancel(SessionId session) {
    return planner.cancel(session);
  }
}
