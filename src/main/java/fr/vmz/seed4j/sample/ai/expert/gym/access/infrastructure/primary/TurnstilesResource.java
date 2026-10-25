package fr.vmz.seed4j.sample.ai.expert.gym.access.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.access.application.AccessApplicationService;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gym/access")
class TurnstilesResource {

  private final AccessApplicationService access;

  TurnstilesResource(AccessApplicationService access) {
    this.access = access;
  }

  @PostMapping("/gyms/{gym}/entries")
  RestEntry enter(@PathVariable Gym gym, @RequestBody RestBadge badge) {
    return RestEntry.from(access.enter(badge.toDomain(), gym));
  }

  @PostMapping("/exits")
  RestPresence exit(@RequestBody RestBadge badge) {
    return RestPresence.from(access.exit(badge.toDomain()));
  }

  @GetMapping("/gyms/{gym}/occupancy")
  RestOccupancy occupancy(@PathVariable Gym gym) {
    return RestOccupancy.from(access.occupancy(gym));
  }
}
