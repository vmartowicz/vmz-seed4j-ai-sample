package fr.vmz.seed4j.sample.ai.expert.gym.schedule.infrastructure.secondary.session;

import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.ClassSession;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.ClassSessionsRepository;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.SessionId;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryClassSessionsRepository implements ClassSessionsRepository {

  private final Map<SessionId, ClassSession> sessions = new ConcurrentHashMap<>();

  @Override
  public void save(ClassSession session) {
    sessions.put(session.id(), session);
  }

  @Override
  public Optional<ClassSession> get(SessionId session) {
    return Optional.ofNullable(sessions.get(session));
  }

  @Override
  public List<ClassSession> ofWeek(LocalDate monday) {
    var nextMonday = monday.plusWeeks(1).atStartOfDay();

    return sessions
      .values()
      .stream()
      .filter(session -> !session.start().isBefore(monday.atStartOfDay()) && session.start().isBefore(nextMonday))
      .toList();
  }
}
