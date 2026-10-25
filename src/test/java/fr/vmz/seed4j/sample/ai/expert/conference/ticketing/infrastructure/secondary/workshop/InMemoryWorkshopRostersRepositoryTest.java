package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.infrastructure.secondary.workshop;

import static fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.TicketsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.WorkshopRoster;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryWorkshopRostersRepositoryTest {

  private final InMemoryWorkshopRostersRepository rosters = new InMemoryWorkshopRostersRepository();

  @Test
  void shouldGetSavedRoster() {
    var roster = new WorkshopRoster(workshop());

    rosters.save(roster);

    assertThat(rosters.get(roster.workshop())).containsSame(roster);
    assertThat(rosters.get(workshop())).isEmpty();
  }
}
