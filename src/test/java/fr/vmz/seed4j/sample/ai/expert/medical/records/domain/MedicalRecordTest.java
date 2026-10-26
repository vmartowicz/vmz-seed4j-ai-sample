package fr.vmz.seed4j.sample.ai.expert.medical.records.domain;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import org.junit.jupiter.api.Test;

@UnitTest
class MedicalRecordTest {

  private final MedicalRecord record = MedicalRecord.of(PatientId.newId());

  @Test
  void shouldKeepAllergiesHistoryAndTreatments() {
    record.addAllergy(" Penicillin ");
    record.addHistory("Appendectomy in 2010");
    record.addTreatment("Levothyrox 50");

    assertThat(record.allergies()).containsExactly("penicillin");
    assertThat(record.history()).containsExactly("Appendectomy in 2010");
    assertThat(record.treatments()).containsExactly("Levothyrox 50");
    assertThat(record.isAllergicTo("PENICILLIN")).isTrue();
    assertThat(record.isAllergicTo("Amoxicillin")).isFalse();
  }

  @Test
  void shouldDeclarePacemaker() {
    assertThat(record.hasPacemaker()).isFalse();

    record.declarePacemaker();

    assertThat(record.hasPacemaker()).isTrue();
  }

  @Test
  void shouldNotAddBlankAllergy() {
    assertThatThrownBy(() -> record.addAllergy(" ")).isInstanceOf(AssertionException.class);
  }
}
