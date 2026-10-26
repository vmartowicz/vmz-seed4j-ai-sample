package fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain;

import java.util.List;
import java.util.Optional;

public interface ExaminationsRepository {
  void save(Examination examination);

  Optional<Examination> get(ExamId examination);

  List<Examination> all();

  default Examination examinationOf(ExamId examination) {
    return get(examination).orElseThrow(() -> new UnknownExaminationException(examination));
  }
}
