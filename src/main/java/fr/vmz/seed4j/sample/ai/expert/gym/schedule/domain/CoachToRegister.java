package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.Set;

public record CoachToRegister(String name, Set<Discipline> certifications) {
  public CoachToRegister {
    Assert.field("name", name).notBlank().maxLength(100);
    Assert.notEmpty("certifications", certifications);
  }

  public Coach register() {
    return new Coach(CoachId.newId(), name.trim(), Set.copyOf(certifications));
  }
}
