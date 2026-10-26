package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.infrastructure.secondary.sample;

import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.LabSample;
import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.LabSamplesRepository;
import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.SampleId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryLabSamplesRepository implements LabSamplesRepository {

  private final Map<SampleId, LabSample> samples = new ConcurrentHashMap<>();

  @Override
  public void save(LabSample sample) {
    samples.put(sample.id(), sample);
  }

  @Override
  public Optional<LabSample> get(SampleId sample) {
    return Optional.ofNullable(samples.get(sample));
  }

  @Override
  public List<LabSample> ofPatient(PatientId patient) {
    return samples
      .values()
      .stream()
      .filter(sample -> sample.patient().equals(patient))
      .toList();
  }
}
