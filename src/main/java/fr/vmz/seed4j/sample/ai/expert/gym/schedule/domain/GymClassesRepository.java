package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

import java.util.List;

public interface GymClassesRepository {
  void save(GymClass gymClass);

  List<GymClass> all();
}
