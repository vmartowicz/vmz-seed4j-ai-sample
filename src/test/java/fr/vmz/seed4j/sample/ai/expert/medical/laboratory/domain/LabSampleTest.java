package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain;

import static fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.LabFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.math.BigDecimal;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class LabSampleTest {

  private final PatientId patient = PatientId.newId();

  @Nested
  class Taking {

    @Test
    void shouldTakePrescribedSample() {
      var prescription = prescription(patient, NOW.toLocalDate().minusMonths(11), "glucose", "POTASSIUM");

      var sample = LabSample.take(new SampleToTake(patient, prescription.id(), LabAnalysis.GLUCOSE, true), prescription, NOW);

      assertThat(sample.id()).isNotNull();
      assertThat(sample.patient()).isEqualTo(patient);
      assertThat(sample.prescription()).isEqualTo(prescription.id());
      assertThat(sample.prescriber()).isEqualTo(prescription.prescriber());
      assertThat(sample.test()).isEqualTo(LabAnalysis.GLUCOSE);
      assertThat(sample.takenAt()).isEqualTo(NOW);
      assertThat(sample.status()).isEqualTo(SampleStatus.TAKEN);
      assertThat(sample.value()).isEmpty();
    }

    @Test
    void shouldNotTakeSampleWithPrescriptionOlderThanOneYear() {
      var prescription = prescription(patient, NOW.toLocalDate().minusYears(1).minusDays(1), "POTASSIUM");

      assertThatThrownBy(() -> LabSample.take(new SampleToTake(patient, prescription.id(), LabAnalysis.POTASSIUM, false), prescription, NOW))
        .isExactlyInstanceOf(LabPrescriptionExpiredException.class);
      assertThat(LabSample.take(new SampleToTake(patient, prescription.id(), LabAnalysis.POTASSIUM, false), prescription, NOW.minusDays(1)).status()).isEqualTo(
        SampleStatus.TAKEN
      );
    }

    @Test
    void shouldNotTakeTestNotPrescribedForThePatient() {
      var prescription = prescription(patient, NOW.toLocalDate(), "POTASSIUM");
      var otherPatient = prescription(PatientId.newId(), NOW.toLocalDate(), "GLUCOSE");

      assertThatThrownBy(() -> LabSample.take(new SampleToTake(patient, prescription.id(), LabAnalysis.GLUCOSE, true), prescription, NOW))
        .isExactlyInstanceOf(TestNotPrescribedException.class)
        .hasMessageContaining("GLUCOSE");
      assertThatThrownBy(() -> LabSample.take(new SampleToTake(patient, otherPatient.id(), LabAnalysis.GLUCOSE, true), otherPatient, NOW)).isExactlyInstanceOf(
        TestNotPrescribedException.class
      );
    }

    @Test
    void shouldRefuseFastingTestWhenPatientIsNotFasting() {
      var prescription = prescription(patient, NOW.toLocalDate(), "LIPID_PROFILE");

      assertThatThrownBy(() -> LabSample.take(new SampleToTake(patient, prescription.id(), LabAnalysis.LIPID_PROFILE, false), prescription, NOW))
        .isExactlyInstanceOf(FastingRequiredException.class)
        .hasMessageContaining("fasting");
    }
  }

  @Nested
  class Analysis {

    @Test
    void shouldAnalyseWithin4Hours() {
      var sample = taken(LabAnalysis.POTASSIUM);

      sample.analyse(new BigDecimal("4.2"), NOW.plusHours(4));

      assertThat(sample.status()).isEqualTo(SampleStatus.ANALYSED);
      assertThat(sample.value()).contains(new BigDecimal("4.2"));
    }

    @Test
    void shouldAskForNewSampleAfter4Hours() {
      var sample = taken(LabAnalysis.POTASSIUM);

      assertThatThrownBy(() -> sample.analyse(new BigDecimal("4.2"), NOW.plusHours(4).plusMinutes(1)))
        .isExactlyInstanceOf(SampleExpiredException.class)
        .hasMessageContaining("taken again");
    }

    @Test
    void shouldValidateAnalysedSample() {
      var sample = taken(LabAnalysis.POTASSIUM);
      sample.analyse(new BigDecimal("6.5"), NOW.plusHours(1));

      sample.validate("Dr Bio", NOW.plusHours(2));

      assertThat(sample.status()).isEqualTo(SampleStatus.VALIDATED);
      assertThat(sample.validatedBy()).contains("Dr Bio");
      assertThat(sample.validatedAt()).contains(NOW.plusHours(2));
      assertThat(sample.isAbnormal()).isTrue();
      assertThat(sample.isCritical()).isTrue();
    }

    @Test
    void shouldFollowSampleLifecycle() {
      var sample = taken(LabAnalysis.POTASSIUM);

      assertThatThrownBy(() -> sample.validate("Dr Bio", NOW)).isExactlyInstanceOf(InvalidSampleTransitionException.class);
      assertThatThrownBy(() -> sample.announce(DoctorId.newId())).isExactlyInstanceOf(InvalidSampleTransitionException.class);
      assertThat(sample.isAbnormal()).isFalse();
      assertThat(sample.isCritical()).isFalse();
      sample.analyse(BigDecimal.ONE, NOW);
      assertThatThrownBy(() -> sample.analyse(BigDecimal.ONE, NOW)).isExactlyInstanceOf(InvalidSampleTransitionException.class);
    }

    @Test
    void shouldNotValidateWithoutBiologist() {
      var sample = taken(LabAnalysis.POTASSIUM);
      sample.analyse(BigDecimal.ONE, NOW);

      assertThatThrownBy(() -> sample.validate(" ", NOW)).isInstanceOf(fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException.class);
    }
  }

  @Nested
  class PatientVisibility {

    @Test
    void shouldShowNormalValidatedResultToPatient() {
      var sample = taken(LabAnalysis.CREATININE);
      sample.analyse(new BigDecimal("9"), NOW);

      assertThat(sample.isVisibleToPatient()).isFalse();
      sample.validate("Dr Bio", NOW);
      assertThat(sample.isVisibleToPatient()).isTrue();
    }

    @Test
    void shouldShowAbnormalResultOnlyOnceAnnouncedByDoctor() {
      var sample = taken(LabAnalysis.CREATININE);
      sample.analyse(new BigDecimal("20"), NOW);
      sample.validate("Dr Bio", NOW);
      var doctor = DoctorId.newId();

      assertThat(sample.isVisibleToPatient()).isFalse();
      sample.announce(doctor);
      assertThat(sample.isVisibleToPatient()).isTrue();
      assertThat(sample.announcedBy()).contains(doctor);
    }
  }

  @Test
  void shouldRememberAlertedDoctor() {
    var sample = taken(LabAnalysis.POTASSIUM);
    var doctor = DoctorId.newId();

    sample.alerted(doctor);

    assertThat(sample.alertedDoctor()).contains(doctor);
  }
}
