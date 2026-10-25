package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

import java.util.Optional;

public interface CoachesRepository {
  void save(Coach coach);

  Optional<Coach> get(CoachId coach);

  default Coach coach(CoachId coach) {
    return get(coach).orElseThrow(() -> new UnknownCoachException(coach));
  }
}
