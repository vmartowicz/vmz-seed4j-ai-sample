package fr.vmz.seed4j.sample.ai.expert.medical.imaging.infrastructure.secondary.examination;

import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ExamId;
import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.Examination;
import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ExaminationsRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryExaminationsRepository implements ExaminationsRepository {

  private final Map<ExamId, Examination> examinations = new ConcurrentHashMap<>();

  @Override
  public void save(Examination examination) {
    examinations.put(examination.id(), examination);
  }

  @Override
  public Optional<Examination> get(ExamId examination) {
    return Optional.ofNullable(examinations.get(examination));
  }

  @Override
  public List<Examination> all() {
    return List.copyOf(examinations.values());
  }
}
