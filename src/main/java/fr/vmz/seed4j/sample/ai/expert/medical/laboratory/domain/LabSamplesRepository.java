package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.util.List;
import java.util.Optional;

public interface LabSamplesRepository {
  void save(LabSample sample);

  Optional<LabSample> get(SampleId sample);

  List<LabSample> ofPatient(PatientId patient);

  default LabSample sampleOf(SampleId sample) {
    return get(sample).orElseThrow(() -> new UnknownSampleException(sample));
  }
}
