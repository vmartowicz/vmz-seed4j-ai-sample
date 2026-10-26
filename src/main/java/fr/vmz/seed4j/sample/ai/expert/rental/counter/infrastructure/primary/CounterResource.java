package fr.vmz.seed4j.sample.ai.expert.rental.counter.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.rental.counter.application.CounterApplicationService;
import fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.ContractId;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rental/counter")
class CounterResource {

  private final CounterApplicationService counter;

  CounterResource(CounterApplicationService counter) {
    this.counter = counter;
  }

  @PostMapping("/pick-ups")
  @ResponseStatus(HttpStatus.CREATED)
  RestContract pickUp(@RequestBody RestPickUp pickUp) {
    return RestContract.from(counter.pickUp(pickUp.reservation()));
  }

  @PostMapping("/contracts/{contractId}/return")
  RestContract giveBack(@PathVariable UUID contractId, @RequestBody RestReturnInspection inspection) {
    return RestContract.from(counter.giveBack(new ContractId(contractId), inspection.toDomain()));
  }

  @GetMapping("/contracts/{contractId}")
  RestContract contract(@PathVariable UUID contractId) {
    return RestContract.from(counter.contract(new ContractId(contractId)));
  }
}
