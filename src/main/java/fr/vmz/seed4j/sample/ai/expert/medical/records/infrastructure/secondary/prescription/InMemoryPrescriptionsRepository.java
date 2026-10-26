package fr.vmz.seed4j.sample.ai.expert.medical.records.infrastructure.secondary.prescription;

import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.Prescription;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.PrescriptionId;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.PrescriptionsRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryPrescriptionsRepository implements PrescriptionsRepository {

  private final Map<PrescriptionId, Prescription> prescriptions = new ConcurrentHashMap<>();

  @Override
  public void save(Prescription prescription) {
    prescriptions.put(prescription.id(), prescription);
  }

  @Override
  public Optional<Prescription> get(PrescriptionId prescription) {
    return Optional.ofNullable(prescriptions.get(prescription));
  }
}
