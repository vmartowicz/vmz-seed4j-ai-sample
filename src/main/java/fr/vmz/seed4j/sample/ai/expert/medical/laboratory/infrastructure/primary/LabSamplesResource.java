package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.application.LaboratoryApplicationService;
import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.SampleId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
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
@RequestMapping("/api/medical/laboratory")
class LabSamplesResource {

  private final LaboratoryApplicationService laboratory;

  LabSamplesResource(LaboratoryApplicationService laboratory) {
    this.laboratory = laboratory;
  }

  @PostMapping("/samples")
  @ResponseStatus(HttpStatus.CREATED)
  RestLabSample take(@RequestBody RestSampleToTake sample) {
    return RestLabSample.from(laboratory.take(sample.toDomain()));
  }

  @GetMapping("/samples/{sampleId}")
  RestLabSample sample(@PathVariable UUID sampleId) {
    return RestLabSample.from(laboratory.sample(new SampleId(sampleId)));
  }

  @PostMapping("/samples/{sampleId}/analysis")
  RestLabSample analyse(@PathVariable UUID sampleId, @RequestBody RestAnalysis analysis) {
    return RestLabSample.from(laboratory.analyse(new SampleId(sampleId), analysis.value()));
  }

  @PostMapping("/samples/{sampleId}/validation")
  RestLabSample validate(@PathVariable UUID sampleId, @RequestBody RestValidation validation) {
    return RestLabSample.from(laboratory.validate(new SampleId(sampleId), validation.biologist()));
  }

  @PostMapping("/samples/{sampleId}/announcement")
  RestLabSample announce(@PathVariable UUID sampleId, @RequestBody RestAnnouncement announcement) {
    return RestLabSample.from(laboratory.announce(new SampleId(sampleId), new DoctorId(announcement.doctorId())));
  }

  @GetMapping("/patients/{patientId}/results")
  List<RestLabSample> results(@PathVariable UUID patientId) {
    return laboratory.resultsOf(new PatientId(patientId)).stream().map(RestLabSample::from).toList();
  }
}
