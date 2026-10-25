package fr.vmz.seed4j.sample.ai.expert.conference.program.infrastructure.secondary.session;

import fr.vmz.seed4j.sample.ai.expert.conference.program.domain.ProgramSession;
import fr.vmz.seed4j.sample.ai.expert.conference.program.domain.ProgramSessionsRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryProgramSessionsRepository implements ProgramSessionsRepository {

  private final Map<UUID, ProgramSession> sessions = new ConcurrentHashMap<>();

  @Override
  public void save(ProgramSession session) {
    sessions.put(session.talk().talk(), session);
  }

  @Override
  public Optional<ProgramSession> get(UUID talk) {
    return Optional.ofNullable(sessions.get(talk));
  }

  @Override
  public void delete(UUID talk) {
    sessions.remove(talk);
  }

  @Override
  public List<ProgramSession> all() {
    return List.copyOf(sessions.values());
  }
}
