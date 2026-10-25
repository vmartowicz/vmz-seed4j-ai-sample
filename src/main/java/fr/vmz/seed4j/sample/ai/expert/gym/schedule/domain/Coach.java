package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

import java.util.Set;

public record Coach(CoachId id, String name, Set<Discipline> certifications) {
  public boolean isCertifiedFor(Discipline discipline) {
    return certifications.contains(discipline);
  }
}
