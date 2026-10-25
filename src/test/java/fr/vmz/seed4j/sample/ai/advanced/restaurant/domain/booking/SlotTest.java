package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

import static fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.TableBookingsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@UnitTest
class SlotTest {

  @Test
  void shouldBuildSlotInItsService() {
    var slot = dinner();

    assertThat(slot.service()).isEqualTo(MealService.DINNER);
    assertThat(slot.at()).isEqualTo(DINNER_TIME);
  }

  @Test
  void shouldNotBuildSlotWithoutDate() {
    assertThatThrownBy(() -> new Slot(null, LocalTime.NOON)).isInstanceOf(AssertionException.class);
  }

  @Test
  void shouldNotBuildSlotWithoutTime() {
    assertThatThrownBy(() -> new Slot(TUESDAY, null)).isInstanceOf(AssertionException.class);
  }

  @ParameterizedTest
  @ValueSource(ints = { 5, 6 })
  void shouldNotBookWhenRestaurantIsClosed(int daysAfterTuesday) {
    var slot = new Slot(TUESDAY.plusDays(daysAfterTuesday), LocalTime.of(19, 30));

    assertThatThrownBy(() -> slot.assertBookableFrom(NOW)).isExactlyInstanceOf(RestaurantClosedException.class);
    assertThat(slot.isBookableFrom(NOW)).isFalse();
  }

  @Test
  void shouldBookUpTo30DaysAhead() {
    var now = TUESDAY.minusDays(30).atTime(23, 59);

    assertThatCode(() -> dinner().assertBookableFrom(now)).doesNotThrowAnyException();
    assertThat(dinner().isBookableFrom(now)).isTrue();
  }

  @Test
  void shouldNotBookMoreThan30DaysAhead() {
    var now = TUESDAY.minusDays(31).atTime(23, 59);

    assertThatThrownBy(() -> dinner().assertBookableFrom(now)).isExactlyInstanceOf(BookingTooFarException.class);
    assertThat(dinner().isBookableFrom(now)).isFalse();
  }

  @Test
  void shouldBookUntil2HoursBeforeServiceStart() {
    assertThatCode(() -> dinner().assertBookableFrom(TUESDAY.atTime(17, 0))).doesNotThrowAnyException();
  }

  @Test
  void shouldNotBookLessThan2HoursBeforeServiceStart() {
    var tooLate = TUESDAY.atTime(17, 1);

    assertThatThrownBy(() -> dinner().assertBookableFrom(tooLate)).isExactlyInstanceOf(BookingTooLateException.class);
    assertThat(dinner().isBookableFrom(tooLate)).isFalse();
  }
}
