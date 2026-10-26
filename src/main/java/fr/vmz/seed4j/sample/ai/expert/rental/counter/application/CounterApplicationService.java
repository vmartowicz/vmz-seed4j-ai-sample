package fr.vmz.seed4j.sample.ai.expert.rental.counter.application;

import fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.Cashier;
import fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.Contract;
import fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.ContractId;
import fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.ContractsRepository;
import fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.CounterDesk;
import fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.CustomerStatus;
import fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.FleetVehicles;
import fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.Reservations;
import fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.ReturnInspection;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class CounterApplicationService {

  private final CounterDesk desk;
  private final Clock clock;

  public CounterApplicationService(
    ContractsRepository contracts,
    Reservations reservations,
    FleetVehicles fleet,
    CustomerStatus customers,
    Cashier cashier,
    Clock clock
  ) {
    desk = new CounterDesk(contracts, reservations, fleet, customers, cashier);
    this.clock = clock;
  }

  public Contract pickUp(UUID reservation) {
    return desk.pickUp(reservation, LocalDateTime.now(clock));
  }

  public Contract giveBack(ContractId contract, ReturnInspection inspection) {
    return desk.giveBack(contract, inspection, LocalDateTime.now(clock));
  }

  public Contract contract(ContractId contract) {
    return desk.contract(contract);
  }
}
