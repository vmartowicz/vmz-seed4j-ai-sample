package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import java.util.List;

public final class Studios {

  private Studios() {}

  public static List<Studio> of(Gym gym) {
    if (gym == Gym.PART_DIEU) {
      return List.of(new Studio(gym, "A", 30), new Studio(gym, "B", 20), new Studio(gym, "C", 12));
    }

    return List.of(new Studio(gym, "A", 25), new Studio(gym, "B", 15));
  }

  public static Studio studio(Gym gym, String name) {
    Assert.notNull("gym", gym);
    Assert.notBlank("studio", name);

    return of(gym)
      .stream()
      .filter(studio -> studio.name().equals(name))
      .findFirst()
      .orElseThrow(() -> new UnknownStudioException(gym, name));
  }
}
