package fr.vmz.seed4j.sample.ai.expert.medical.records.application;

import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.Consultation;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.ConsultationBilling;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.ConsultationId;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.ConsultationsRepository;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.CareRegistry;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.MedicalRecord;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.MedicalRecordsRepository;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.Practice;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.Prescription;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.PrescriptionId;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.PrescriptionItem;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.PrescriptionsRepository;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.springframework.stereotype.Service;

@Service
public class MedicalRecordsApplicationService {

  private final Practice practice;
  private final MedicalRecordsRepository records;
  private final PrescriptionsRepository prescriptions;
  private final Clock clock;

  public MedicalRecordsApplicationService(
    ConsultationsRepository consultations,
    MedicalRecordsRepository records,
    PrescriptionsRepository prescriptions,
    CareRegistry registry,
    ConsultationBilling billing,
    Clock clock
  ) {
    practice = new Practice(consultations, records, prescriptions, registry, billing);
    this.records = records;
    this.prescriptions = prescriptions;
    this.clock = clock;
  }

  public Consultation open(PatientId patient, DoctorId doctor, String reason) {
    return practice.open(patient, doctor, reason, now());
  }

  public Consultation consultation(ConsultationId consultation) {
    return practice.consultation(consultation);
  }

  public Consultation write(ConsultationId consultation, String examination, String diagnosis) {
    return practice.write(consultation, examination, diagnosis);
  }

  public Prescription prescribe(ConsultationId consultation, List<PrescriptionItem> items) {
    return practice.prescribe(consultation, items, LocalDate.now(clock));
  }

  public Consultation close(ConsultationId consultation) {
    return practice.close(consultation, now());
  }

  public Consultation addAddendum(ConsultationId consultation, DoctorId author, String text) {
    return practice.addAddendum(consultation, author, text, now());
  }

  public MedicalRecord record(PatientId patient) {
    return practice.record(patient);
  }

  public MedicalRecord updateRecord(PatientId patient, Consumer<MedicalRecord> update) {
    return practice.updateRecord(patient, update);
  }

  public Optional<Prescription> findPrescription(PrescriptionId prescription) {
    return prescriptions.get(prescription);
  }

  public boolean hasPacemaker(PatientId patient) {
    return records.get(patient).map(MedicalRecord::hasPacemaker).orElse(false);
  }

  private LocalDateTime now() {
    return LocalDateTime.now(clock);
  }
}
