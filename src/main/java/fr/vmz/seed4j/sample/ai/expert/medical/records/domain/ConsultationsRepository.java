package fr.vmz.seed4j.sample.ai.expert.medical.records.domain;

import java.util.Optional;

public interface ConsultationsRepository {
  void save(Consultation consultation);

  Optional<Consultation> get(ConsultationId consultation);

  default Consultation consultationOf(ConsultationId consultation) {
    return get(consultation).orElseThrow(() -> new UnknownConsultationException(consultation));
  }
}
