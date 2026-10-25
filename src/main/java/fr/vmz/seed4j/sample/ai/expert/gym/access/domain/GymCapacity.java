package fr.vmz.seed4j.sample.ai.expert.gym.access.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;

public final class GymCapacity {

  private static final int PART_DIEU = 150;
  private static final int OTHER_GYMS = 100;

  private GymCapacity() {}

  public static int of(Gym gym) {
    return gym == Gym.PART_DIEU ? PART_DIEU : OTHER_GYMS;
  }
}
