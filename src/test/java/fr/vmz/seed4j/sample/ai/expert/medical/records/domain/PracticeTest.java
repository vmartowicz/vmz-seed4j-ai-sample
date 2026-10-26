package fr.vmz.seed4j.sample.ai.expert.medical.records.domain;

import static fr.vmz.seed4j.sample.ai.expert.medical.records.domain.RecordsFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@UnitTest
class PracticeTest {

  private final ConsultationsRepository consultations = mock(ConsultationsRepository.class, CALLS_REAL_METHODS);
  private final MedicalRecordsRepository records = mock(MedicalRecordsRepository.class);
  private final PrescriptionsRepository prescriptions = mock(PrescriptionsRepository.class, CALLS_REAL_METHODS);
  private final CareRegistry registry = mock(CareRegistry.class);
  private final ConsultationBilling billing = mock(ConsultationBilling.class);
  private final Practice practice = new Practice(consultations, records, prescriptions, registry, billing);

  private final PatientId patient = PatientId.newId();
  private final DoctorId doctor = DoctorId.newId();

  @BeforeEach
  void loadRegistry() {
    doReturn(true).when(registry).patientExists(patient);
    doReturn(true).when(registry).doctorExists(doctor);
    doReturn(Optional.empty()).when(records).get(any());
  }

  @Test
  void shouldOpenConsultationForKnownPatientAndDoctor() {
    var consultation = practice.open(patient, doctor, "Sore throat", NOW);

    verify(consultations).save(consultation);
  }

  @Test
  void shouldNotOpenConsultationForUnknownPatientOrDoctor() {
    assertThatThrownBy(() -> practice.open(PatientId.newId(), doctor, "Sore throat", NOW)).isExactlyInstanceOf(UnknownRecordPatientException.class);
    assertThatThrownBy(() -> practice.open(patient, DoctorId.newId(), "Sore throat", NOW)).isExactlyInstanceOf(UnknownPractitionerException.class);
  }

  @Test
  void shouldPrescribeAgainstMedicalRecord() {
    var consultation = known(practice.open(patient, doctor, "Sore throat", NOW));
    var record = MedicalRecord.of(patient);
    record.addAllergy("Penicillin");
    doReturn(Optional.of(record)).when(records).get(patient);

    var prescription = practice.prescribe(consultation.id(), items(medicine("Doliprane")), TODAY);

    verify(prescriptions).save(prescription);
    verify(consultations, times(2)).save(consultation);
    assertThatThrownBy(() -> practice.prescribe(consultation.id(), items(medicine("Penicillin")), TODAY)).isExactlyInstanceOf(AllergyException.class);
  }

  @Test
  void shouldWriteCloseAndBillConsultation() {
    var consultation = known(practice.open(patient, doctor, "Sore throat", NOW));

    practice.write(consultation.id(), "Red throat", "Pharyngitis");
    practice.close(consultation.id(), NOW.plusMinutes(20));
    practice.addAddendum(consultation.id(), doctor, "Better", NOW.plusDays(1));

    assertThat(consultation.addenda()).hasSize(1);
    verify(billing).closed(consultation);
  }

  @Test
  void shouldUpdateMedicalRecord() {
    var record = practice.updateRecord(patient, current -> current.addAllergy("Latex"));

    assertThat(record.allergies()).containsExactly("latex");
    assertThat(practice.record(patient).allergies()).isEmpty();
    verify(records).save(record);
  }

  @Test
  void shouldNotFindUnknownConsultationOrPrescription() {
    var consultation = ConsultationId.newId();
    doReturn(Optional.empty()).when(consultations).get(consultation);
    var prescription = PrescriptionId.newId();
    doReturn(Optional.empty()).when(prescriptions).get(prescription);

    assertThatThrownBy(() -> practice.consultation(consultation)).isExactlyInstanceOf(UnknownConsultationException.class);
    assertThatThrownBy(() -> practice.prescription(prescription)).isExactlyInstanceOf(UnknownPrescriptionException.class);
  }

  @Test
  void shouldFindPrescription() {
    var consultation = known(practice.open(patient, doctor, "Sore throat", NOW));
    var prescription = practice.prescribe(consultation.id(), items(medicine("Doliprane")), TODAY);
    doReturn(Optional.of(prescription)).when(prescriptions).get(prescription.id());

    assertThat(practice.prescription(prescription.id())).isSameAs(prescription);
  }

  private Consultation known(Consultation consultation) {
    doReturn(Optional.of(consultation)).when(consultations).get(consultation.id());

    return consultation;
  }
}
