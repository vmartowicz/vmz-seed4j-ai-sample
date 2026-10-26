package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.math.BigDecimal;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@UnitTest
class LabAnalysisTest {

  @ParameterizedTest
  @CsvSource(
    {
      "GLUCOSE, 0.40, true, true",
      "GLUCOSE, 0.60, true, false",
      "GLUCOSE, 0.90, false, false",
      "GLUCOSE, 1.20, true, false",
      "LIPID_PROFILE, 1.80, false, false",
      "LIPID_PROFILE, 2.10, true, false",
      "POTASSIUM, 4.00, false, false",
      "POTASSIUM, 5.50, true, false",
      "POTASSIUM, 6.50, true, true",
      "CREATININE, 9, false, false",
      "CREATININE, 13, true, false",
    }
  )
  void shouldInterpretValues(LabAnalysis test, BigDecimal value, boolean abnormal, boolean critical) {
    assertThat(test.isAbnormal(value)).isEqualTo(abnormal);
    assertThat(test.isCritical(value)).isEqualTo(critical);
  }

  @ParameterizedTest
  @CsvSource({ "GLUCOSE, true", "LIPID_PROFILE, true", "POTASSIUM, false", "CREATININE, false" })
  void shouldKnowFastingTests(LabAnalysis test, boolean fasting) {
    assertThat(test.fasting()).isEqualTo(fasting);
  }
}
