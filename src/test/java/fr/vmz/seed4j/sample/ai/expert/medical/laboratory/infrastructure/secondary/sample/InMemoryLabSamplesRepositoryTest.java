package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.infrastructure.secondary.sample;

import static fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.LabFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.LabAnalysis;
import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.SampleId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryLabSamplesRepositoryTest {

  @Test
  void shouldGetSavedSamples() {
    var samples = new InMemoryLabSamplesRepository();
    var sample = taken(LabAnalysis.GLUCOSE);
    samples.save(sample);
    samples.save(taken(LabAnalysis.GLUCOSE));

    assertThat(samples.get(sample.id())).containsSame(sample);
    assertThat(samples.get(SampleId.newId())).isEmpty();
    assertThat(samples.ofPatient(sample.patient())).containsExactly(sample);
    assertThat(samples.ofPatient(PatientId.newId())).isEmpty();
  }
}
