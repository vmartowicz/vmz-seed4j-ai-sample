package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ClassSessionsRepository {
  void save(ClassSession session);

  Optional<ClassSession> get(SessionId session);

  List<ClassSession> ofWeek(LocalDate monday);

  default ClassSession session(SessionId session) {
    return get(session).orElseThrow(() -> new UnknownSessionException(session));
  }
}
