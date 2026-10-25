package fr.vmz.seed4j.sample.ai.advanced.restaurant.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.application.DinersApplicationService;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.DinerEmail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurant/diners")
class DinersResource {

  private final DinersApplicationService diners;

  DinersResource(DinersApplicationService diners) {
    this.diners = diners;
  }

  @GetMapping("/{email}")
  RestDiner diner(@PathVariable String email) {
    return RestDiner.from(diners.diner(new DinerEmail(email)), diners.currentYear());
  }
}
