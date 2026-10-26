package fr.vmz.seed4j.sample.ai.expert.bank.compliance.infrastructure.secondary.sanction;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain.SanctionedName;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemorySanctionsRepositoryTest {

  @Test
  void shouldListSanctionedNames() {
    var sanctions = new InMemorySanctionsRepository();

    sanctions.save(new SanctionedName("Evil Corp"));

    assertThat(sanctions.all()).containsExactly(new SanctionedName("Evil Corp"));
  }
}
