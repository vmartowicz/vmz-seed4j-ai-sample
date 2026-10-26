package fr.vmz.seed4j.sample.ai.expert.rental.counter.domain;

import java.util.Optional;

public interface ContractsRepository {
  void save(Contract contract);

  Optional<Contract> get(ContractId contract);

  default Contract contract(ContractId contract) {
    return get(contract).orElseThrow(() -> new UnknownContractException(contract));
  }
}
