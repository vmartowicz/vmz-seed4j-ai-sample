package fr.vmz.seed4j.sample.ai.expert.medical.imaging.infrastructure.secondary.examination;

import static fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ImagingFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ExamId;
import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ImagingExam;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryExaminationsRepositoryTest {

  @Test
  void shouldGetSavedExaminations() {
    var examinations = new InMemoryExaminationsRepository();
    var examination = performed(ImagingExam.XRAY);

    examinations.save(examination);

    assertThat(examinations.get(examination.id())).containsSame(examination);
    assertThat(examinations.get(ExamId.newId())).isEmpty();
    assertThat(examinations.all()).containsExactly(examination);
  }
}
