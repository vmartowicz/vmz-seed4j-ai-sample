package fr.vmz.seed4j.sample.ai.shared.medical.domain;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import org.junit.jupiter.api.Test;

@UnitTest
class MedicalKernelTest {

  @Test
  void shouldGenerateDistinctIds() {
    assertThat(PatientId.newId()).isNotEqualTo(PatientId.newId());
    assertThat(DoctorId.newId().get()).isNotNull();
  }
}
