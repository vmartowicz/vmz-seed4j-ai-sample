package fr.vmz.seed4j.sample.ai.clinic.infrastructure.secondary.vet;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.clinic.domain.Pagination;
import fr.vmz.seed4j.sample.ai.clinic.domain.vet.Specialty;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryVetsRepositoryTest {

  private final InMemoryVetsRepository vets = new InMemoryVetsRepository();

  @Test
  void shouldPaginateVetsBy5SortedByLastName() {
    var firstPage = vets.list(new Pagination(1));

    assertThat(firstPage.content())
      .extracting(vet -> vet.lastName().get())
      .containsExactly("Carter", "Douglas", "Jenkins", "Leary", "Ortega");
    assertThat(firstPage.totalElements()).isEqualTo(6);
    assertThat(vets.list(new Pagination(2)).content()).extracting(vet -> vet.lastName().get()).containsExactly("Stevens");
  }

  @Test
  void shouldListVetsWithNoneOneOrSeveralSpecialties() {
    var vetsOfFirstPage = vets.list(new Pagination(1)).content();

    assertThat(vetsOfFirstPage.get(0).specialties()).isEmpty();
    assertThat(vetsOfFirstPage.get(1).specialties()).containsExactly(Specialty.SURGERY, Specialty.DENTISTRY);
    assertThat(vetsOfFirstPage.get(3).specialties()).containsExactly(Specialty.RADIOLOGY);
  }
}
