package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public final class LabSample {

  private static final int ANALYSIS_DELAY_HOURS = 4;

  private final SampleId id;
  private final PatientId patient;
  private final UUID prescription;
  private final DoctorId prescriber;
  private final LabAnalysis test;
  private final LocalDateTime takenAt;
  private SampleStatus status;
  private @Nullable BigDecimal value;
  private @Nullable String validatedBy;
  private @Nullable LocalDateTime validatedAt;
  private @Nullable DoctorId alertedDoctor;
  private @Nullable DoctorId announcedBy;

  private LabSample(SampleToTake sample, LabPrescription from, LocalDateTime now) {
    id = SampleId.newId();
    patient = sample.patient();
    prescription = from.id();
    prescriber = from.prescriber();
    test = sample.test();
    takenAt = now;
    status = SampleStatus.TAKEN;
  }

  public static LabSample take(SampleToTake sample, LabPrescription prescription, LocalDateTime now) {
    if (!prescription.prescribes(sample.patient(), sample.test())) {
      throw new TestNotPrescribedException(prescription.id(), sample.test());
    }

    if (prescription.isExpiredOn(now.toLocalDate())) {
      throw new LabPrescriptionExpiredException(prescription.id());
    }

    if (sample.test().fasting() && !sample.fasting()) {
      throw new FastingRequiredException(sample.test());
    }

    return new LabSample(sample, prescription, now);
  }

  public void analyse(BigDecimal result, LocalDateTime now) {
    assertStatus(SampleStatus.TAKEN, "analyse");
    if (now.isAfter(takenAt.plusHours(ANALYSIS_DELAY_HOURS))) {
      throw new SampleExpiredException(id);
    }

    value = result;
    status = SampleStatus.ANALYSED;
  }

  public void validate(String biologist, LocalDateTime now) {
    assertStatus(SampleStatus.ANALYSED, "validate");
    Assert.notBlank("biologist", biologist);

    validatedBy = biologist;
    validatedAt = now;
    status = SampleStatus.VALIDATED;
  }

  public void announce(DoctorId doctor) {
    assertStatus(SampleStatus.VALIDATED, "announce");

    announcedBy = doctor;
  }

  public void alerted(DoctorId doctor) {
    alertedDoctor = doctor;
  }

  public boolean isAbnormal() {
    return value().map(test::isAbnormal).orElse(false);
  }

  public boolean isCritical() {
    return value().map(test::isCritical).orElse(false);
  }

  public boolean isVisibleToPatient() {
    return status == SampleStatus.VALIDATED && (!isAbnormal() || announcedBy != null);
  }

  private void assertStatus(SampleStatus expected, String action) {
    if (status != expected) {
      throw new InvalidSampleTransitionException(id, status, action);
    }
  }

  public SampleId id() {
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

  public LabAnalysis test() {
    return test;
  }

  public LocalDateTime takenAt() {
    return takenAt;
  }

  public SampleStatus status() {
    return status;
  }

  public Optional<BigDecimal> value() {
    return Optional.ofNullable(value);
  }

  public Optional<String> validatedBy() {
    return Optional.ofNullable(validatedBy);
  }

  public Optional<LocalDateTime> validatedAt() {
    return Optional.ofNullable(validatedAt);
  }

  public Optional<DoctorId> alertedDoctor() {
    return Optional.ofNullable(alertedDoctor);
  }

  public Optional<DoctorId> announcedBy() {
    return Optional.ofNullable(announcedBy);
  }
}
