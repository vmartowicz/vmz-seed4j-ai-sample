package fr.vmz.seed4j.sample.ai.expert.conference.cfp.infrastructure.secondary.committee;

import static fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.ProposalsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.CommitteeMemberId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryCommitteeMembersRepositoryTest {

  private final InMemoryCommitteeMembersRepository members = new InMemoryCommitteeMembersRepository();

  @Test
  void shouldGetSavedMember() {
    var member = member("initech");

    members.save(member);

    assertThat(members.get(member.id())).contains(member);
    assertThat(members.get(CommitteeMemberId.newId())).isEmpty();
  }
}
