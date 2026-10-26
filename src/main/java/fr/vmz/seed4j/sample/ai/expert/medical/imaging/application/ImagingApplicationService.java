package fr.vmz.seed4j.sample.ai.expert.medical.imaging.application;

import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ExamId;
import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ExamToPerform;
import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.Examination;
import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ExaminationsRepository;
import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ImagingDepartment;
import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ImagingPatients;
import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ImagingPrescriptions;
import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.Implants;
import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.KidneyFunction;
import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ReportDelivery;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ImagingApplicationService {

  private final ImagingDepartment department;
  private final Clock clock;

  public ImagingApplicationService(
    ExaminationsRepository examinations,
    ImagingPrescriptions prescriptions,
    ImagingPatients patients,
    Implants implants,
    KidneyFunction kidneys,
    ReportDelivery delivery,
    Clock clock
  ) {
    department = new ImagingDepartment(examinations, prescriptions, patients, implants, kidneys, delivery);
    this.clock = clock;
  }

  public Examination perform(ExamToPerform request) {
    return department.perform(request, now());
  }

  public Examination report(ExamId examination, String text) {
    return department.report(examination, text, now());
  }

  public List<Examination> overdue() {
    return department.overdue(now());
  }

  public Examination examination(ExamId examination) {
    return department.examination(examination);
  }

  private LocalDateTime now() {
    return LocalDateTime.now(clock);
  }
}
