package fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain;

import java.time.LocalDateTime;
import java.util.List;

public class ImagingDepartment {

  private static final int CREATININE_VALIDITY_MONTHS = 3;

  private final ExaminationsRepository examinations;
  private final ImagingPrescriptions prescriptions;
  private final ImagingPatients patients;
  private final Implants implants;
  private final KidneyFunction kidneys;
  private final ReportDelivery delivery;

  public ImagingDepartment(
    ExaminationsRepository examinations,
    ImagingPrescriptions prescriptions,
    ImagingPatients patients,
    Implants implants,
    KidneyFunction kidneys,
    ReportDelivery delivery
  ) {
    this.examinations = examinations;
    this.prescriptions = prescriptions;
    this.patients = patients;
    this.implants = implants;
    this.kidneys = kidneys;
    this.delivery = delivery;
  }

  public Examination perform(ExamToPerform request, LocalDateTime now) {
    var prescription = prescriptions
      .prescriptionOf(request.prescription())
      .orElseThrow(() -> new UnknownImagingPrescriptionException(request.prescription()));

    var examination = Examination.perform(request, prescription, safetyOf(request, now), now);
    examinations.save(examination);

    return examination;
  }

  private PatientSafety safetyOf(ExamToPerform request, LocalDateTime now) {
    var patient = patients.profileOf(request.patient()).orElseThrow(() -> new UnknownImagingPatientException(request.patient()));

    return new PatientSafety(
      patient.female(),
      patient.birthDate(),
      implants.hasPacemaker(request.patient()),
      kidneys.hasCreatinineSince(request.patient(), now.toLocalDate().minusMonths(CREATININE_VALIDITY_MONTHS))
    );
  }

  public Examination report(ExamId examination, String text, LocalDateTime now) {
    var found = examinations.examinationOf(examination);
    found.report(text, now);
    delivery.deliver(found);
    examinations.save(found);

    return found;
  }

  public List<Examination> overdue(LocalDateTime now) {
    return examinations
      .all()
      .stream()
      .filter(examination -> examination.isOverdue(now))
      .toList();
  }

  public Examination examination(ExamId examination) {
    return examinations.examinationOf(examination);
  }
}
