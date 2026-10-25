package fr.vmz.seed4j.sample.ai.expert.gym.access.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.Occupancy;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;

record RestOccupancy(Gym gym, long inside, int capacity) {
  static RestOccupancy from(Occupancy occupancy) {
    return new RestOccupancy(occupancy.gym(), occupancy.inside(), occupancy.capacity());
  }
}
