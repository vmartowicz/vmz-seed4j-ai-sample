package fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain;

import static fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ImagingFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@UnitTest
class ImagingDepartmentTest {

  private final ExaminationsRepository examinations = mock(ExaminationsRepository.class, CALLS_REAL_METHODS);
  private final ImagingPrescriptions prescriptions = mock(ImagingPrescriptions.class);
  private final ImagingPatients patients = mock(ImagingPatients.class);
  private final Implants implants = mock(Implants.class);
  private final KidneyFunction kidneys = mock(KidneyFunction.class);
  private final ReportDelivery delivery = mock(ReportDelivery.class);
  private final ImagingDepartment department = new ImagingDepartment(examinations, prescriptions, patients, implants, kidneys, delivery);

  private final PatientId patient = PatientId.newId();

  @BeforeEach
  void loadPatient() {
    doReturn(Optional.of(new ImagingPatient(true, LocalDate.of(1990, 5, 4)))).when(patients).profileOf(patient);
  }

  @Test
  void shouldGatherPatientSafetyBeforePerforming() {
    var prescription = known(prescription(patient, TODAY, "CT_CONTRAST", "MRI"));
    doReturn(true).when(kidneys).hasCreatinineSince(patient, TODAY.minusMonths(3));

    var examination = department.perform(new ExamToPerform(patient, prescription.id(), ImagingExam.CT_CONTRAST, true), NOW);

    verify(examinations).save(examination);
    doReturn(true).when(implants).hasPacemaker(patient);
    assertThatThrownBy(() -> department.perform(new ExamToPerform(patient, prescription.id(), ImagingExam.MRI, false), NOW)).isExactlyInstanceOf(
      PacemakerException.class
    );
  }

  @Test
  void shouldNotPerformWithUnknownPrescriptionOrPatient() {
    var unknown = UUID.randomUUID();
    doReturn(Optional.empty()).when(prescriptions).prescriptionOf(unknown);
    var stranger = PatientId.newId();
    var prescription = known(prescription(stranger, TODAY, "XRAY"));
    doReturn(Optional.empty()).when(patients).profileOf(stranger);

    assertThatThrownBy(() -> department.perform(new ExamToPerform(patient, unknown, ImagingExam.XRAY, true), NOW)).isExactlyInstanceOf(
      UnknownImagingPrescriptionException.class
    );
    assertThatThrownBy(() -> department.perform(new ExamToPerform(stranger, prescription.id(), ImagingExam.XRAY, true), NOW)).isExactlyInstanceOf(
      UnknownImagingPatientException.class
    );
  }

  @Test
  void shouldDeliverReportToPrescriberAndPatientTogether() {
    var examination = knownExamination(performed(ImagingExam.XRAY));

    department.report(examination.id(), "No fracture", NOW.plusHours(2));

    verify(delivery).deliver(examination);
    verify(examinations).save(examination);
  }

  @Test
  void shouldListOverdueExaminations() {
    var overdue = performed(ImagingExam.XRAY);
    var reported = performed(ImagingExam.XRAY);
    reported.report("Normal", NOW.plusHours(1));
    doReturn(List.of(overdue, reported)).when(examinations).all();

    assertThat(department.overdue(NOW.plusDays(3))).containsExactly(overdue);
  }

  @Test
  void shouldFindExamination() {
    var examination = knownExamination(performed(ImagingExam.MRI));
    var unknown = ExamId.newId();
    doReturn(Optional.empty()).when(examinations).get(unknown);

    assertThat(department.examination(examination.id())).isSameAs(examination);
    assertThatThrownBy(() -> department.examination(unknown)).isExactlyInstanceOf(UnknownExaminationException.class);
  }

  private ImagingPrescription known(ImagingPrescription prescription) {
    doReturn(Optional.of(prescription)).when(prescriptions).prescriptionOf(prescription.id());

    return prescription;
  }

  private Examination knownExamination(Examination examination) {
    doReturn(Optional.of(examination)).when(examinations).get(examination.id());

    return examination;
  }
}
