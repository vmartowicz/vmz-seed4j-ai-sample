package fr.vmz.seed4j.sample.ai.intermediate.clinic.infrastructure.secondary.owner;

import static fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.OwnersFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.Pagination;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.Owner;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.OwnerId;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.OwnersSearch;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryOwnersRepositoryTest {

  private static final Pagination FIRST_PAGE = new Pagination(1);

  private final InMemoryOwnersRepository owners = new InMemoryOwnersRepository();

  @Test
  void shouldGetSavedOwner() {
    var owner = owner();
    owners.save(owner);

    assertThat(owners.get(owner.id())).contains(owner);
  }

  @Test
  void shouldNotGetUnknownOwner() {
    assertThat(owners.get(OwnerId.newId())).isEmpty();
  }

  @Test
  void shouldSearchByLastNamePrefixIgnoringCase() {
    var franklin = save("George", "Franklin");
    save("Betty", "Davis");

    assertThat(owners.search(new OwnersSearch("fr", FIRST_PAGE)).content()).containsExactly(franklin);
  }

  @Test
  void shouldReturnAllOwnersWithoutLastName() {
    save("George", "Franklin");
    save("Betty", "Davis");

    assertThat(owners.search(new OwnersSearch("", FIRST_PAGE)).totalElements()).isEqualTo(2);
  }

  @Test
  void shouldSortByLastNameThenFirstName() {
    var franklin = save("George", "Franklin");
    var bettyDavis = save("Betty", "Davis");
    var aliceDavis = save("Alice", "Davis");

    assertThat(owners.search(new OwnersSearch("", FIRST_PAGE)).content()).containsExactly(aliceDavis, bettyDavis, franklin);
  }

  @Test
  void shouldPaginateBy5() {
    IntStream.rangeClosed(1, 7).forEach(index -> save("Owner" + index, "Davis"));

    var page = owners.search(new OwnersSearch("dav", new Pagination(2)));

    assertThat(page.content()).extracting(owner -> owner.information().firstName().get()).containsExactly("Owner6", "Owner7");
    assertThat(page.totalElements()).isEqualTo(7);
  }

  @Test
  void shouldGetEmptyPageWithoutMatchingOwner() {
    save("George", "Franklin");

    var page = owners.search(new OwnersSearch("Dav", FIRST_PAGE));

    assertThat(page.content()).isEmpty();
    assertThat(page.totalElements()).isZero();
  }

  private Owner save(String firstName, String lastName) {
    var owner = Owner.register(ownerInformation(firstName, lastName));
    owners.save(owner);

    return owner;
  }
}
