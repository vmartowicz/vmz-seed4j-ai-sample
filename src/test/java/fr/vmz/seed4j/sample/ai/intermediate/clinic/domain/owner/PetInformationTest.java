package fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

@UnitTest
class PetInformationTest {

  @Test
  void shouldNotBuildWithoutType() {
    assertThatThrownBy(() -> new PetInformation(new PetName("Leo"), LocalDate.of(2020, 9, 7), null))
      .isExactlyInstanceOf(MissingMandatoryValueException.class)
      .hasMessageContaining("type");
  }

  @Test
  void shouldNotBuildWithoutBirthDate() {
    assertThatThrownBy(() -> new PetInformation(new PetName("Leo"), null, PetType.CAT))
      .isExactlyInstanceOf(MissingMandatoryValueException.class)
      .hasMessageContaining("birthDate");
  }
}
