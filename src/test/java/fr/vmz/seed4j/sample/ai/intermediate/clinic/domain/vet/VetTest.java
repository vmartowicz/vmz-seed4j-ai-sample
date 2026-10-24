package fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.vet;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.FirstName;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.LastName;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import org.junit.jupiter.api.Test;

@UnitTest
class VetTest {

  @Test
  void shouldBuildVetWithoutSpecialty() {
    var vet = Vet.builder().id(VetId.newId()).firstName(new FirstName("James")).lastName(new LastName("Carter")).build();

    assertThat(vet.id()).isNotNull();
    assertThat(vet.firstName()).isEqualTo(new FirstName("James"));
    assertThat(vet.lastName()).isEqualTo(new LastName("Carter"));
    assertThat(vet.specialties()).isEmpty();
  }

  @Test
  void shouldSortSpecialties() {
    var vet = Vet.builder()
      .id(VetId.newId())
      .firstName(new FirstName("Linda"))
      .lastName(new LastName("Douglas"))
      .specialties(Specialty.DENTISTRY, Specialty.SURGERY, Specialty.DENTISTRY)
      .build();

    assertThat(vet.specialties()).containsExactly(Specialty.SURGERY, Specialty.DENTISTRY);
  }

  @Test
  void shouldNotBuildWithoutLastName() {
    var builder = Vet.builder().id(VetId.newId()).firstName(new FirstName("James"));

    assertThatThrownBy(builder::build).isExactlyInstanceOf(MissingMandatoryValueException.class).hasMessageContaining("lastName");
  }
}
