package fr.vmz.seed4j.sample.ai.expert.gym.schedule.infrastructure.secondary.gymclass;

import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.GymClass;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.GymClassId;
import fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.GymClassesRepository;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryGymClassesRepository implements GymClassesRepository {

  private final Map<GymClassId, GymClass> classes = new ConcurrentHashMap<>();

  @Override
  public void save(GymClass gymClass) {
    classes.put(gymClass.id(), gymClass);
  }

  @Override
  public List<GymClass> all() {
    return List.copyOf(classes.values());
  }
}
