package fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner;

import static fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.OwnersFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.Pagination;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

@UnitTest
class OwnersSearchTest {

  private static final Pagination FIRST_PAGE = new Pagination(1);

  @Test
  void shouldMatchLastNamePrefixIgnoringCase() {
    assertThat(new OwnersSearch("fRA", FIRST_PAGE).matches(owner())).isTrue();
  }

  @Test
  void shouldNotMatchAnotherLastName() {
    assertThat(new OwnersSearch("Dav", FIRST_PAGE).matches(owner())).isFalse();
  }

  @Test
  void shouldNotMatchLastNameContainingPrefix() {
    assertThat(new OwnersSearch("klin", FIRST_PAGE).matches(owner())).isFalse();
  }

  @Test
  void shouldMatchAllOwnersWithoutLastName() {
    assertThat(new OwnersSearch(" ", FIRST_PAGE).matches(owner())).isTrue();
  }

  @Test
  void shouldSortOwnersByLastNameThenFirstName() {
    var franklinGeorge = Owner.register(ownerInformation("George", "Franklin"));
    var davisBetty = Owner.register(ownerInformation("Betty", "Davis"));
    var davisAlice = Owner.register(ownerInformation("alice", "davis"));

    assertThat(Stream.of(franklinGeorge, davisBetty, davisAlice).sorted(OwnersSearch.ORDER)).containsExactly(
      davisAlice,
      davisBetty,
      franklinGeorge
    );
  }
}
