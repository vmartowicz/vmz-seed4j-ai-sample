package fr.vmz.seed4j.sample.ai.expert.medical.records.infrastructure.secondary.consultation;

import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.Consultation;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.ConsultationId;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.ConsultationsRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryConsultationsRepository implements ConsultationsRepository {

  private final Map<ConsultationId, Consultation> consultations = new ConcurrentHashMap<>();

  @Override
  public void save(Consultation consultation) {
    consultations.put(consultation.id(), consultation);
  }

  @Override
  public Optional<Consultation> get(ConsultationId consultation) {
    return Optional.ofNullable(consultations.get(consultation));
  }
}
