package fr.vmz.seed4j.sample.ai.expert.medical.records.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

public class Practice {

  private final ConsultationsRepository consultations;
  private final MedicalRecordsRepository records;
  private final PrescriptionsRepository prescriptions;
  private final CareRegistry registry;
  private final ConsultationBilling billing;

  public Practice(
    ConsultationsRepository consultations,
    MedicalRecordsRepository records,
    PrescriptionsRepository prescriptions,
    CareRegistry registry,
    ConsultationBilling billing
  ) {
    this.consultations = consultations;
    this.records = records;
    this.prescriptions = prescriptions;
    this.registry = registry;
    this.billing = billing;
  }

  public Consultation open(PatientId patient, DoctorId doctor, String reason, LocalDateTime now) {
    assertKnownPatient(patient);
    assertKnownDoctor(doctor);

    var consultation = Consultation.open(patient, doctor, reason, now);
    consultations.save(consultation);

    return consultation;
  }

  public Consultation write(ConsultationId consultation, String examination, String diagnosis) {
    var found = consultations.consultationOf(consultation);
    found.write(examination, diagnosis);
    consultations.save(found);

    return found;
  }

  public Prescription prescribe(ConsultationId consultation, List<PrescriptionItem> items, LocalDate today) {
    var found = consultations.consultationOf(consultation);

    var prescription = found.prescribe(items, record(found.patient()), today);
    prescriptions.save(prescription);
    consultations.save(found);

    return prescription;
  }

  public Consultation close(ConsultationId consultation, LocalDateTime now) {
    var found = consultations.consultationOf(consultation);
    found.close(now);
    consultations.save(found);
    billing.closed(found);

    return found;
  }

  public Consultation addAddendum(ConsultationId consultation, DoctorId author, String text, LocalDateTime now) {
    assertKnownDoctor(author);
    var found = consultations.consultationOf(consultation);
    found.addAddendum(author, text, now);
    consultations.save(found);

    return found;
  }

  public MedicalRecord record(PatientId patient) {
    assertKnownPatient(patient);

    return records.get(patient).orElseGet(() -> MedicalRecord.of(patient));
  }

  public MedicalRecord updateRecord(PatientId patient, Consumer<MedicalRecord> update) {
    var record = record(patient);
    update.accept(record);
    records.save(record);

    return record;
  }

  public Consultation consultation(ConsultationId consultation) {
    return consultations.consultationOf(consultation);
  }

  public Prescription prescription(PrescriptionId prescription) {
    return prescriptions.prescriptionOf(prescription);
  }

  private void assertKnownPatient(PatientId patient) {
    if (!registry.patientExists(patient)) {
      throw new UnknownRecordPatientException(patient);
    }
  }

  private void assertKnownDoctor(DoctorId doctor) {
    if (!registry.doctorExists(doctor)) {
      throw new UnknownPractitionerException(doctor);
    }
  }
}
