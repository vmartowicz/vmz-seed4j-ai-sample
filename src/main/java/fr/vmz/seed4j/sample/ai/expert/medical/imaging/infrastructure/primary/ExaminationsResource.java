package fr.vmz.seed4j.sample.ai.expert.medical.imaging.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.imaging.application.ImagingApplicationService;
import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ExamId;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/medical/imaging/examinations")
class ExaminationsResource {

  private final ImagingApplicationService imaging;

  ExaminationsResource(ImagingApplicationService imaging) {
    this.imaging = imaging;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  RestExamination perform(@RequestBody RestExamToPerform request) {
    return RestExamination.from(imaging.perform(request.toDomain()));
  }

  @GetMapping("/overdue")
  List<RestExamination> overdue() {
    return imaging.overdue().stream().map(RestExamination::from).toList();
  }

  @GetMapping("/{examinationId}")
  RestExamination examination(@PathVariable UUID examinationId) {
    return RestExamination.from(imaging.examination(new ExamId(examinationId)));
  }

  @PostMapping("/{examinationId}/report")
  RestExamination report(@PathVariable UUID examinationId, @RequestBody RestReport report) {
    return RestExamination.from(imaging.report(new ExamId(examinationId), report.text()));
  }
}
