package fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain;

import static fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ImagingFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@UnitTest
class ExaminationTest {

  private final PatientId patient = PatientId.newId();

  @Nested
  class Performing {

    @Test
    void shouldPerformPrescribedExamWithImagesAvailableImmediately() {
      var prescription = prescription(patient, TODAY.minusYears(1), "xray");

      var examination = perform(prescription, ImagingExam.XRAY, false, man());

      assertThat(examination.id()).isNotNull();
      assertThat(examination.patient()).isEqualTo(patient);
      assertThat(examination.prescription()).isEqualTo(prescription.id());
      assertThat(examination.prescriber()).isEqualTo(prescription.prescriber());
      assertThat(examination.exam()).isEqualTo(ImagingExam.XRAY);
      assertThat(examination.performedAt()).isEqualTo(NOW);
      assertThat(examination.imagesAvailable()).isTrue();
      assertThat(examination.status()).isEqualTo(ExaminationStatus.PERFORMED);
      assertThat(examination.report()).isEmpty();
      assertThat(examination.reportedAt()).isEmpty();
    }

    @Test
    void shouldNotPerformExamWithPrescriptionOlderThanOneYear() {
      var prescription = prescription(patient, TODAY.minusYears(1).minusDays(1), "XRAY");

      assertThatThrownBy(() -> perform(prescription, ImagingExam.XRAY, false, man())).isExactlyInstanceOf(ImagingPrescriptionExpiredException.class);
    }

    @Test
    void shouldNotPerformExamNotPrescribedForThePatient() {
      var prescription = prescription(patient, TODAY, "XRAY");
      var otherPatient = prescription(PatientId.newId(), TODAY, "MRI");

      assertThatThrownBy(() -> perform(prescription, ImagingExam.MRI, false, man())).isExactlyInstanceOf(ExamNotPrescribedException.class);
      assertThatThrownBy(() -> perform(otherPatient, ImagingExam.MRI, false, man())).isExactlyInstanceOf(ExamNotPrescribedException.class);
    }

    @Test
    void shouldNeedRecentCreatinineForContrastScan() {
      var prescription = prescription(patient, TODAY, "CT_CONTRAST");

      assertThatThrownBy(() -> perform(prescription, ImagingExam.CT_CONTRAST, false, man()))
        .isExactlyInstanceOf(CreatinineRequiredException.class)
        .hasMessageContaining("creatinine");
      assertThat(perform(prescription, ImagingExam.CT_CONTRAST, false, new PatientSafety(false, LocalDate.of(1980, 1, 1), false, true)).exam()).isEqualTo(
        ImagingExam.CT_CONTRAST
      );
    }

    @Test
    void shouldRefuseMriToPacemakerWearers() {
      var prescription = prescription(patient, TODAY, "MRI", "ULTRASOUND");
      var pacemaker = new PatientSafety(false, LocalDate.of(1980, 1, 1), true, false);

      assertThatThrownBy(() -> perform(prescription, ImagingExam.MRI, false, pacemaker))
        .isExactlyInstanceOf(PacemakerException.class)
        .hasMessageContaining("pacemaker");
      assertThat(perform(prescription, ImagingExam.ULTRASOUND, false, pacemaker).exam()).isEqualTo(ImagingExam.ULTRASOUND);
    }

    @ParameterizedTest
    @CsvSource(
      {
        "XRAY, true, 1990-05-04, true",
        "CT, true, 2011-06-01, true",
        "CT_CONTRAST, true, 1975-06-02, true",
        "XRAY, true, 2011-06-02, false",
        "XRAY, true, 1975-06-01, false",
        "XRAY, false, 1990-05-04, false",
        "ULTRASOUND, true, 1990-05-04, false",
        "MRI, true, 1990-05-04, false",
      }
    )
    void shouldNeedNegativePregnancyTestForIrradiatingExamsOfWomenFrom15To50(ImagingExam exam, boolean female, LocalDate birthDate, boolean needed) {
      var prescription = prescription(patient, TODAY, exam.name());
      var safety = new PatientSafety(female, birthDate, false, true);

      if (needed) {
        assertThatThrownBy(() -> perform(prescription, exam, false, safety))
          .isExactlyInstanceOf(PregnancyTestRequiredException.class)
          .hasMessageContaining("pregnancy");
      } else {
        assertThat(perform(prescription, exam, false, safety).exam()).isEqualTo(exam);
      }
      assertThat(perform(prescription, exam, true, safety).exam()).isEqualTo(exam);
    }

    private Examination perform(ImagingPrescription prescription, ImagingExam exam, boolean negativePregnancyTest, PatientSafety safety) {
      return Examination.perform(new ExamToPerform(patient, prescription.id(), exam, negativePregnancyTest), prescription, safety, NOW);
    }
  }

  @Nested
  class Reporting {

    @Test
    void shouldReportOnce() {
      var examination = performed(ImagingExam.XRAY);

      examination.report("No fracture", NOW.plusHours(3));

      assertThat(examination.status()).isEqualTo(ExaminationStatus.REPORTED);
      assertThat(examination.report()).contains("No fracture");
      assertThat(examination.reportedAt()).contains(NOW.plusHours(3));
      assertThatThrownBy(() -> examination.report("Again", NOW.plusHours(4))).isExactlyInstanceOf(ExaminationAlreadyReportedException.class);
    }

    @Test
    void shouldNotReportBlankText() {
      var examination = performed(ImagingExam.XRAY);

      assertThatThrownBy(() -> examination.report(" ", NOW)).isInstanceOf(AssertionException.class);
    }

    @Test
    void shouldBeOverdueWithoutReportAfter48Hours() {
      var examination = performed(ImagingExam.MRI);

      assertThat(examination.isOverdue(NOW.plusHours(48))).isFalse();
      assertThat(examination.isOverdue(NOW.plusHours(48).plusMinutes(1))).isTrue();
      examination.report("Normal", NOW.plusHours(49));
      assertThat(examination.isOverdue(NOW.plusHours(49))).isFalse();
    }
  }

  @ParameterizedTest
  @CsvSource({ "XRAY, true", "ULTRASOUND, false", "CT, true", "CT_CONTRAST, true", "MRI, false" })
  void shouldKnowIrradiatingExams(ImagingExam exam, boolean irradiating) {
    assertThat(exam.irradiating()).isEqualTo(irradiating);
  }
}
