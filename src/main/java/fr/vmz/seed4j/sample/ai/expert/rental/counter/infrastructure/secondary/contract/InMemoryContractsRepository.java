package fr.vmz.seed4j.sample.ai.expert.rental.counter.infrastructure.secondary.contract;

import fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.Contract;
import fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.ContractId;
import fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.ContractsRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryContractsRepository implements ContractsRepository {

  private final Map<ContractId, Contract> contracts = new ConcurrentHashMap<>();

  @Override
  public void save(Contract contract) {
    contracts.put(contract.id(), contract);
  }

  @Override
  public Optional<Contract> get(ContractId contract) {
    return Optional.ofNullable(contracts.get(contract));
  }
}
