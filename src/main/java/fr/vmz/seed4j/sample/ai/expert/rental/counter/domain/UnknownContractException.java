package fr.vmz.seed4j.sample.ai.expert.rental.counter.domain;

class UnknownContractException extends UnknownCounterElementException {

  UnknownContractException(ContractId contract) {
    super("Unknown contract " + contract.get());
  }
}
