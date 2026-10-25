package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.infrastructure.secondary.workshop;

import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.WorkshopRoster;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.WorkshopRostersRepository;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryWorkshopRostersRepository implements WorkshopRostersRepository {

  private final Map<UUID, WorkshopRoster> rosters = new ConcurrentHashMap<>();

  @Override
  public void save(WorkshopRoster roster) {
    rosters.put(roster.workshop(), roster);
  }

  @Override
  public Optional<WorkshopRoster> get(UUID workshop) {
    return Optional.ofNullable(rosters.get(workshop));
  }
}
