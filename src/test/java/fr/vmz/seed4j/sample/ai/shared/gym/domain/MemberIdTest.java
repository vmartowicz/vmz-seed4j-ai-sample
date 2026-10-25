package fr.vmz.seed4j.sample.ai.shared.gym.domain;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@UnitTest
class MemberIdTest {

  @Test
  void shouldGenerateDistinctIds() {
    assertThat(MemberId.newId()).isNotEqualTo(MemberId.newId());
  }

  @Test
  void shouldGetId() {
    var id = UUID.randomUUID();

    assertThat(new MemberId(id).get()).isEqualTo(id);
  }

  @Test
  void shouldNotBuildWithoutId() {
    assertThatThrownBy(() -> new MemberId(null)).isInstanceOf(AssertionException.class);
  }
}
