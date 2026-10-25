package fr.vmz.seed4j.sample.ai.expert.gym.schedule.infrastructure.secondary.coach;

import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.Coach;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.CoachId;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.CoachesRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryCoachesRepository implements CoachesRepository {

  private final Map<CoachId, Coach> coaches = new ConcurrentHashMap<>();

  @Override
  public void save(Coach coach) {
    coaches.put(coach.id(), coach);
  }

  @Override
  public Optional<Coach> get(CoachId coach) {
    return Optional.ofNullable(coaches.get(coach));
  }
}
