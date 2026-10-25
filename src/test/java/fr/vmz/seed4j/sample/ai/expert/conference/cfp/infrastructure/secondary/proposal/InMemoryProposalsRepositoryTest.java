package fr.vmz.seed4j.sample.ai.expert.conference.cfp.infrastructure.secondary.proposal;

import static fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.ProposalsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.ProposalId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryProposalsRepositoryTest {

  private final InMemoryProposalsRepository proposals = new InMemoryProposalsRepository();

  @Test
  void shouldGetSavedProposals() {
    var proposal = submitted();

    proposals.save(proposal);

    assertThat(proposals.get(proposal.id())).containsSame(proposal);
    assertThat(proposals.get(ProposalId.newId())).isEmpty();
    assertThat(proposals.all()).containsExactly(proposal);
  }
}
