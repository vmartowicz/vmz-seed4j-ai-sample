package fr.vmz.seed4j.sample.ai.expert.medical.records.infrastructure.secondary.consultation;

import static fr.vmz.seed4j.sample.ai.expert.medical.records.domain.RecordsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.ConsultationId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryConsultationsRepositoryTest {

  @Test
  void shouldGetSavedConsultation() {
    var consultations = new InMemoryConsultationsRepository();
    var consultation = openConsultation();

    consultations.save(consultation);

    assertThat(consultations.get(consultation.id())).containsSame(consultation);
    assertThat(consultations.get(ConsultationId.newId())).isEmpty();
  }
}
