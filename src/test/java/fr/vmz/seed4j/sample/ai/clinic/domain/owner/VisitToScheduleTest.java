package fr.vmz.seed4j.sample.ai.clinic.domain.owner;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import org.junit.jupiter.api.Test;

@UnitTest
class VisitToScheduleTest {

  @Test
  void shouldNotBuildWithoutDate() {
    assertThatThrownBy(() -> new VisitToSchedule(null, new VisitDescription("Rabies shot")))
      .isExactlyInstanceOf(MissingMandatoryValueException.class)
      .hasMessageContaining("date");
  }
}
