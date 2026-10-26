package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.application;

import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.CriticalAlerts;
import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.LabPrescriptions;
import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.LabSample;
import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.LabSamplesRepository;
import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.Laboratory;
import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.Prescribers;
import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.SampleId;
import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.SampleToTake;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LaboratoryApplicationService {

  private final Laboratory laboratory;
  private final Clock clock;

  public LaboratoryApplicationService(LabSamplesRepository samples, LabPrescriptions prescriptions, Prescribers prescribers, CriticalAlerts alerts, Clock clock) {
    laboratory = new Laboratory(samples, prescriptions, prescribers, alerts);
    this.clock = clock;
  }

  public LabSample take(SampleToTake sample) {
    return laboratory.take(sample, now());
  }

  public LabSample analyse(SampleId sample, BigDecimal value) {
    return laboratory.analyse(sample, value, now());
  }

  public LabSample validate(SampleId sample, String biologist) {
    return laboratory.validate(sample, biologist, now());
  }

  public LabSample announce(SampleId sample, DoctorId doctor) {
    return laboratory.announce(sample, doctor);
  }

  public LabSample sample(SampleId sample) {
    return laboratory.sample(sample);
  }

  public List<LabSample> resultsOf(PatientId patient) {
    return laboratory.resultsOf(patient);
  }

  public boolean hasCreatinineSince(PatientId patient, LocalDate since) {
    return laboratory.hasCreatinineSince(patient, since);
  }

  private LocalDateTime now() {
    return LocalDateTime.now(clock);
  }
}
