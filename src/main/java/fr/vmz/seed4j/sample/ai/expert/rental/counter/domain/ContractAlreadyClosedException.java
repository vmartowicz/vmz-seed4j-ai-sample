package fr.vmz.seed4j.sample.ai.expert.rental.counter.domain;

class ContractAlreadyClosedException extends CounterRuleViolationException {

  ContractAlreadyClosedException(ContractId contract) {
    super("Contract " + contract.get() + " is already closed");
  }
}
