package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Laboratory {

  private static final int OPENING_HOUR = 8;
  private static final int CLOSING_HOUR = 20;

  private final LabSamplesRepository samples;
  private final LabPrescriptions prescriptions;
  private final Prescribers prescribers;
  private final CriticalAlerts alerts;

  public Laboratory(LabSamplesRepository samples, LabPrescriptions prescriptions, Prescribers prescribers, CriticalAlerts alerts) {
    this.samples = samples;
    this.prescriptions = prescriptions;
    this.prescribers = prescribers;
    this.alerts = alerts;
  }

  public LabSample take(SampleToTake sample, LocalDateTime now) {
    var prescription = prescriptions
      .prescriptionOf(sample.prescription())
      .orElseThrow(() -> new UnknownLabPrescriptionException(sample.prescription()));

    var taken = LabSample.take(sample, prescription, now);
    samples.save(taken);

    return taken;
  }

  public LabSample analyse(SampleId sample, BigDecimal value, LocalDateTime now) {
    var found = samples.sampleOf(sample);
    found.analyse(value, now);
    samples.save(found);

    return found;
  }

  public LabSample validate(SampleId sample, String biologist, LocalDateTime now) {
    var found = samples.sampleOf(sample);
    found.validate(biologist, now);
    if (found.isCritical()) {
      alert(found, now);
    }
    samples.save(found);

    return found;
  }

  private void alert(LabSample sample, LocalDateTime now) {
    var recipient = recipientOf(sample, now);
    sample.alerted(recipient);
    alerts.send(new CriticalAlert(sample.id(), sample.patient(), sample.test(), sample.value().orElseThrow(), recipient, now));
  }

  private DoctorId recipientOf(LabSample sample, LocalDateTime now) {
    if (isOpen(now)) {
      return sample.prescriber();
    }

    return prescribers.onCallReplacing(sample.prescriber()).orElse(sample.prescriber());
  }

  private static boolean isOpen(LocalDateTime now) {
    var weekend = now.getDayOfWeek() == DayOfWeek.SATURDAY || now.getDayOfWeek() == DayOfWeek.SUNDAY;

    return !weekend && now.getHour() >= OPENING_HOUR && now.getHour() < CLOSING_HOUR;
  }

  public LabSample announce(SampleId sample, DoctorId doctor) {
    var found = samples.sampleOf(sample);
    found.announce(doctor);
    samples.save(found);

    return found;
  }

  public List<LabSample> resultsOf(PatientId patient) {
    return samples.ofPatient(patient).stream().filter(LabSample::isVisibleToPatient).toList();
  }

  public boolean hasCreatinineSince(PatientId patient, LocalDate since) {
    return samples
      .ofPatient(patient)
      .stream()
      .filter(sample -> sample.test() == LabAnalysis.CREATININE)
      .flatMap(sample -> sample.validatedAt().stream())
      .anyMatch(validatedAt -> !validatedAt.toLocalDate().isBefore(since));
  }

  public LabSample sample(SampleId sample) {
    return samples.sampleOf(sample);
  }
}
