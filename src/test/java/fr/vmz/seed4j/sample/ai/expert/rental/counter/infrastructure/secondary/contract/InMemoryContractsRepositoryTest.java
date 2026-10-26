package fr.vmz.seed4j.sample.ai.expert.rental.counter.infrastructure.secondary.contract;

import static fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.CounterFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.ContractId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryContractsRepositoryTest {

  private final InMemoryContractsRepository contracts = new InMemoryContractsRepository();

  @Test
  void shouldGetSavedContract() {
    var contract = open();

    contracts.save(contract);

    assertThat(contracts.get(contract.id())).containsSame(contract);
    assertThat(contracts.get(ContractId.newId())).isEmpty();
  }
}
