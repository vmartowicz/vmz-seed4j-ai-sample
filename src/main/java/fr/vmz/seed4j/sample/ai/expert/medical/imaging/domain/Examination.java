package fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public final class Examination {

  private static final int REPORT_DELAY_HOURS = 48;

  private final ExamId id;
  private final PatientId patient;
  private final UUID prescription;
  private final DoctorId prescriber;
  private final ImagingExam exam;
  private final LocalDateTime performedAt;
  private @Nullable String report;
  private @Nullable LocalDateTime reportedAt;

  private Examination(ExamToPerform request, ImagingPrescription from, LocalDateTime now) {
    id = ExamId.newId();
    patient = request.patient();
    prescription = from.id();
    prescriber = from.prescriber();
    exam = request.exam();
    performedAt = now;
  }

  public static Examination perform(ExamToPerform request, ImagingPrescription prescription, PatientSafety safety, LocalDateTime now) {
    if (!prescription.prescribes(request.patient(), request.exam())) {
      throw new ExamNotPrescribedException(prescription.id(), request.exam());
    }

    if (prescription.isExpiredOn(now.toLocalDate())) {
      throw new ImagingPrescriptionExpiredException(prescription.id());
    }

    if (request.exam() == ImagingExam.MRI && safety.pacemaker()) {
      throw new PacemakerException();
    }

    if (request.exam() == ImagingExam.CT_CONTRAST && !safety.recentCreatinine()) {
      throw new CreatinineRequiredException();
    }

    if (request.exam().irradiating() && safety.mayBePregnantOn(now.toLocalDate()) && !request.negativePregnancyTest()) {
      throw new PregnancyTestRequiredException(request.exam());
    }

    return new Examination(request, prescription, now);
  }

  public void report(String text, LocalDateTime now) {
    if (report != null) {
      throw new ExaminationAlreadyReportedException(id);
    }
    Assert.notBlank("report", text);

    report = text;
    reportedAt = now;
  }

  public boolean isOverdue(LocalDateTime now) {
    return report == null && now.isAfter(performedAt.plusHours(REPORT_DELAY_HOURS));
  }

  public boolean imagesAvailable() {
    return true;
  }

  public ExaminationStatus status() {
    return report == null ? ExaminationStatus.PERFORMED : ExaminationStatus.REPORTED;
  }

  public ExamId id() {
    return id;
  }

  public PatientId patient() {
    return patient;
  }

  public UUID prescription() {
    return prescription;
  }

  public DoctorId prescriber() {
    return prescriber;
  }

  public ImagingExam exam() {
    return exam;
  }

  public LocalDateTime performedAt() {
    return performedAt;
  }

  public Optional<String> report() {
    return Optional.ofNullable(report);
  }

  public Optional<LocalDateTime> reportedAt() {
    return Optional.ofNullable(reportedAt);
  }
}
