package fr.vmz.seed4j.sample.ai.expert.medical.records.domain;

import static fr.vmz.seed4j.sample.ai.expert.medical.records.domain.RecordsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@UnitTest
class ConsultationTest {

  @Test
  void shouldWriteOpenConsultation() {
    var consultation = openConsultation();

    consultation.write("Red throat", "Pharyngitis");

    assertThat(consultation.id()).isNotNull();
    assertThat(consultation.reason()).isEqualTo("Sore throat");
    assertThat(consultation.openedAt()).isEqualTo(NOW);
    assertThat(consultation.examination()).contains("Red throat");
    assertThat(consultation.diagnosis()).contains("Pharyngitis");
    assertThat(consultation.status()).isEqualTo(ConsultationStatus.OPEN);
    assertThat(consultation.prescription()).isEmpty();
  }

  @Test
  void shouldPrescribe() {
    var consultation = openConsultation();

    var prescription = consultation.prescribe(items(medicine("Amoxicillin"), new PrescriptionItem(ItemKind.LAB_TEST, "GLUCOSE")), MedicalRecord.of(consultation.patient()), TODAY);

    assertThat(prescription.patient()).isEqualTo(consultation.patient());
    assertThat(prescription.prescriber()).isEqualTo(consultation.doctor());
    assertThat(prescription.issuedOn()).isEqualTo(TODAY);
    assertThat(prescription.items()).hasSize(2);
    assertThat(consultation.prescription()).contains(prescription.id());
  }

  @Test
  void shouldBlockMedicineThePatientIsAllergicTo() {
    var consultation = openConsultation();
    var record = MedicalRecord.of(consultation.patient());
    record.addAllergy("Amoxicillin");

    assertThatThrownBy(() -> consultation.prescribe(items(medicine("amoxicillin")), record, TODAY))
      .isExactlyInstanceOf(AllergyException.class)
      .hasMessageContaining("amoxicillin");
    assertThat(consultation.prescribe(items(new PrescriptionItem(ItemKind.LAB_TEST, "AMOXICILLIN")), record, TODAY).items()).hasSize(1);
  }

  @Test
  void shouldNotBeValidForAnotherItemKind() {
    var consultation = openConsultation();
    var prescription = consultation.prescribe(items(medicine("Glucose")), MedicalRecord.of(consultation.patient()), TODAY);

    assertThat(prescription.isValidFor(ItemKind.LAB_TEST, "Glucose", TODAY)).isFalse();
  }

  @Test
  void shouldNotChangeClosedConsultation() {
    var consultation = openConsultation();
    consultation.close(NOW.plusMinutes(20));

    assertThat(consultation.status()).isEqualTo(ConsultationStatus.CLOSED);
    assertThat(consultation.closedAt()).contains(NOW.plusMinutes(20));
    assertThatThrownBy(() -> consultation.write("Other", "Other")).isExactlyInstanceOf(ConsultationClosedException.class);
    assertThatThrownBy(() -> consultation.prescribe(items(medicine("Doliprane")), MedicalRecord.of(consultation.patient()), TODAY)).isExactlyInstanceOf(
      ConsultationClosedException.class
    );
    assertThatThrownBy(() -> consultation.close(NOW)).isExactlyInstanceOf(ConsultationClosedException.class);
  }

  @Test
  void shouldAddSignedAddendumToClosedConsultation() {
    var consultation = openConsultation();
    consultation.close(NOW);

    consultation.addAddendum(consultation.doctor(), "Fever gone", NOW.plusDays(2));

    assertThat(consultation.addenda()).containsExactly(new Addendum(consultation.doctor(), "Fever gone", NOW.plusDays(2)));
  }

  @Test
  void shouldAddAddendumOnlyToClosedConsultation() {
    var consultation = openConsultation();

    assertThatThrownBy(() -> consultation.addAddendum(DoctorId.newId(), "Too early", NOW)).isExactlyInstanceOf(ConsultationOpenException.class);
  }

  @Test
  void shouldNotPrescribeNothing() {
    var consultation = openConsultation();

    assertThatThrownBy(() -> consultation.prescribe(items(), MedicalRecord.of(consultation.patient()), TODAY)).isInstanceOf(AssertionException.class);
  }

  @ParameterizedTest
  @CsvSource({ "MEDICINE, 2026-09-01", "LAB_TEST, 2027-06-01", "IMAGING, 2027-06-01" })
  void shouldKeepPrescriptionValidAccordingToItemKind(ItemKind kind, String lastDay) {
    var consultation = openConsultation();
    var prescription = consultation.prescribe(items(new PrescriptionItem(kind, "CODE")), MedicalRecord.of(consultation.patient()), TODAY);

    assertThat(prescription.isValidFor(kind, "code", LocalDate.parse(lastDay))).isTrue();
    assertThat(prescription.isValidFor(kind, "CODE", LocalDate.parse(lastDay).plusDays(1))).isFalse();
    assertThat(prescription.isValidFor(kind, "OTHER", TODAY)).isFalse();
  }
}
