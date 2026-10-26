package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain;

import static fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.LabFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@UnitTest
class LaboratoryTest {

  private final LabSamplesRepository samples = mock(LabSamplesRepository.class, CALLS_REAL_METHODS);
  private final LabPrescriptions prescriptions = mock(LabPrescriptions.class);
  private final Prescribers prescribers = mock(Prescribers.class);
  private final CriticalAlerts alerts = mock(CriticalAlerts.class);
  private final Laboratory laboratory = new Laboratory(samples, prescriptions, prescribers, alerts);

  private final PatientId patient = PatientId.newId();

  @Test
  void shouldTakeSampleOnKnownPrescription() {
    var prescription = known(prescription(patient, NOW.toLocalDate(), "POTASSIUM"));

    var sample = laboratory.take(new SampleToTake(patient, prescription.id(), LabAnalysis.POTASSIUM, false), NOW);

    assertThat(sample.prescriber()).isEqualTo(prescription.prescriber());
    verify(samples).save(sample);
  }

  @Test
  void shouldNotTakeSampleOnUnknownPrescription() {
    var unknown = UUID.randomUUID();
    doReturn(Optional.empty()).when(prescriptions).prescriptionOf(unknown);

    assertThatThrownBy(() -> laboratory.take(new SampleToTake(patient, unknown, LabAnalysis.POTASSIUM, false), NOW)).isExactlyInstanceOf(
      UnknownLabPrescriptionException.class
    );
  }

  @Test
  void shouldAnalyseAndValidateNormalResultWithoutAlert() {
    var sample = knownSample(LabAnalysis.POTASSIUM);

    laboratory.analyse(sample.id(), new BigDecimal("4.2"), NOW.plusHours(1));
    laboratory.validate(sample.id(), "Dr Bio", NOW.plusHours(2));

    assertThat(sample.status()).isEqualTo(SampleStatus.VALIDATED);
    assertThat(sample.alertedDoctor()).isEmpty();
    verify(samples, times(2)).save(sample);
    verifyNoInteractions(alerts);
  }

  @Test
  void shouldAlertPrescriberOfCriticalResultDuringOpeningHours() {
    var sample = criticalSample();

    laboratory.validate(sample.id(), "Dr Bio", LocalDateTime.of(2026, 6, 1, 19, 59));

    assertThat(sample.alertedDoctor()).contains(sample.prescriber());
    verify(alerts).send(new CriticalAlert(sample.id(), sample.patient(), LabAnalysis.POTASSIUM, new BigDecimal("6.5"), sample.prescriber(), LocalDateTime.of(2026, 6, 1, 19, 59)));
    verifyNoInteractions(prescribers);
  }

  @ParameterizedTest
  @CsvSource({ "2026-06-06T21:00", "2026-06-07T10:00", "2026-06-01T07:59", "2026-06-01T20:00" })
  void shouldAlertOnCallDoctorOfCriticalResultOutsideOpeningHours(LocalDateTime validation) {
    var sample = criticalSample();
    var onCall = DoctorId.newId();
    doReturn(Optional.of(onCall)).when(prescribers).onCallReplacing(sample.prescriber());

    laboratory.validate(sample.id(), "Dr Bio", validation);

    assertThat(sample.alertedDoctor()).contains(onCall);
    verify(alerts).send(any());
  }

  @Test
  void shouldAlertPrescriberWhenNobodyIsOnCall() {
    var sample = criticalSample();
    doReturn(Optional.empty()).when(prescribers).onCallReplacing(sample.prescriber());

    laboratory.validate(sample.id(), "Dr Bio", LocalDateTime.of(2026, 6, 6, 21, 0));

    assertThat(sample.alertedDoctor()).contains(sample.prescriber());
  }

  @Test
  void shouldAnnounceResultAndShowVisibleResultsToPatient() {
    var normal = validated(LabAnalysis.CREATININE, "9");
    var abnormal = validated(LabAnalysis.CREATININE, "20");
    doReturn(List.of(normal, abnormal)).when(samples).ofPatient(patient);

    assertThat(laboratory.resultsOf(patient)).containsExactly(normal);
    laboratory.announce(abnormal.id(), DoctorId.newId());
    assertThat(laboratory.resultsOf(patient)).containsExactly(normal, abnormal);
    verify(samples).save(abnormal);
  }

  @Test
  void shouldFindRecentValidatedCreatinine() {
    var creatinine = validated(LabAnalysis.CREATININE, "9");
    var potassium = validated(LabAnalysis.POTASSIUM, "4");
    var pending = knownSample(LabAnalysis.CREATININE);
    doReturn(List.of(potassium, pending, creatinine)).when(samples).ofPatient(patient);

    assertThat(laboratory.hasCreatinineSince(patient, NOW.toLocalDate())).isTrue();
    assertThat(laboratory.hasCreatinineSince(patient, NOW.toLocalDate().plusDays(1))).isFalse();
    doReturn(List.of(potassium, pending)).when(samples).ofPatient(patient);
    assertThat(laboratory.hasCreatinineSince(patient, LocalDate.MIN)).isFalse();
  }

  @Test
  void shouldNotFindUnknownSample() {
    var unknown = SampleId.newId();
    doReturn(Optional.empty()).when(samples).get(unknown);

    assertThatThrownBy(() -> laboratory.sample(unknown)).isExactlyInstanceOf(UnknownSampleException.class);
    var sample = knownSample(LabAnalysis.GLUCOSE);
    assertThat(laboratory.sample(sample.id())).isSameAs(sample);
  }

  private LabPrescription known(LabPrescription prescription) {
    doReturn(Optional.of(prescription)).when(prescriptions).prescriptionOf(prescription.id());

    return prescription;
  }

  private LabSample knownSample(LabAnalysis test) {
    var prescription = known(prescription(patient, NOW.toLocalDate(), test.name()));
    var sample = LabSample.take(new SampleToTake(patient, prescription.id(), test, true), prescription, NOW);
    doReturn(Optional.of(sample)).when(samples).get(sample.id());

    return sample;
  }

  private LabSample criticalSample() {
    var sample = knownSample(LabAnalysis.POTASSIUM);
    sample.analyse(new BigDecimal("6.5"), NOW);

    return sample;
  }

  private LabSample validated(LabAnalysis test, String value) {
    var sample = knownSample(test);
    sample.analyse(new BigDecimal(value), NOW);
    sample.validate("Dr Bio", NOW);

    return sample;
  }
}
