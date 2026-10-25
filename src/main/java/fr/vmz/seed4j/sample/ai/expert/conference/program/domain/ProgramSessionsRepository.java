package fr.vmz.seed4j.sample.ai.expert.conference.program.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProgramSessionsRepository {
  void save(ProgramSession session);

  Optional<ProgramSession> get(UUID talk);

  void delete(UUID talk);

  List<ProgramSession> all();
}
