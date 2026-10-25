package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

import java.util.Optional;
import java.util.UUID;

public interface WorkshopRostersRepository {
  void save(WorkshopRoster roster);

  Optional<WorkshopRoster> get(UUID workshop);
}
