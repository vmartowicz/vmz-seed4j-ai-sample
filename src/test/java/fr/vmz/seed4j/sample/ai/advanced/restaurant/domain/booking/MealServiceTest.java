package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@UnitTest
class MealServiceTest {

  @Test
  void shouldAcceptLunchArrivalsEvery30MinutesUntil1330() {
    assertThat(MealService.LUNCH.start()).isEqualTo(LocalTime.of(12, 0));
    assertThat(MealService.LUNCH.arrivalTimes()).containsExactly(
      LocalTime.of(12, 0),
      LocalTime.of(12, 30),
      LocalTime.of(13, 0),
      LocalTime.of(13, 30)
    );
  }

  @Test
  void shouldAcceptDinnerArrivalsEvery30MinutesUntil2130() {
    assertThat(MealService.DINNER.arrivalTimes()).hasSize(6).startsWith(LocalTime.of(19, 0)).endsWith(LocalTime.of(21, 30));
  }

  @Test
  void shouldFindServiceOfArrivalTime() {
    assertThat(MealService.of(LocalTime.of(12, 30))).isEqualTo(MealService.LUNCH);
    assertThat(MealService.of(LocalTime.of(21, 30))).isEqualTo(MealService.DINNER);
  }

  @ParameterizedTest
  @ValueSource(strings = { "11:30", "12:15", "14:00", "18:30", "22:00" })
  void shouldNotAcceptArrivalOutsideServiceSlots(String time) {
    assertThatThrownBy(() -> MealService.of(LocalTime.parse(time))).isExactlyInstanceOf(InvalidArrivalTimeException.class).hasMessageContaining(
      time
    );
  }
}
