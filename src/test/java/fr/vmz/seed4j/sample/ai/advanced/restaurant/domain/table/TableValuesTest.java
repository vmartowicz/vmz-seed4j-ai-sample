package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@UnitTest
class TableValuesTest {

  @ParameterizedTest
  @ValueSource(ints = { 0, 19 })
  void shouldNotBuildTableNumberOutsideRoom(int number) {
    assertThatThrownBy(() -> new TableNumber(number)).isInstanceOf(AssertionException.class);
  }

  @Test
  void shouldBuildTableNumber() {
    assertThat(new TableNumber(18).get()).isEqualTo(18);
  }

  @ParameterizedTest
  @ValueSource(ints = { 1, 9 })
  void shouldNotBuildPartySizeOutside2To8(int people) {
    assertThatThrownBy(() -> new PartySize(people)).isInstanceOf(AssertionException.class);
  }

  @Test
  void shouldRequireDepositFrom6People() {
    assertThat(new PartySize(5).requiresDeposit()).isFalse();
    assertThat(new PartySize(6).requiresDeposit()).isTrue();
    assertThat(new PartySize(8).get()).isEqualTo(8);
  }

  @Test
  void shouldRecognizeCouples() {
    assertThat(new PartySize(2).isCouple()).isTrue();
    assertThat(new PartySize(3).isCouple()).isFalse();
  }
}
