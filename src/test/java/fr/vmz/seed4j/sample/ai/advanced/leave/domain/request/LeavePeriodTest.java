package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.LeaveDays;
import static fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.HalfDay.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class LeavePeriodTest {

  // 2026-04-06 is a Monday
  private static final LocalDate MONDAY = LocalDate.of(2026, 4, 6);
  private static final LocalDate FRIDAY = MONDAY.plusDays(4);

  @Nested
  class Days {

    @Test
    void shouldCountFullDay() {
      assertThat(period(MONDAY, MORNING, MONDAY, AFTERNOON).days()).isEqualTo(days("1"));
    }

    @Test
    void shouldCountMorning() {
      assertThat(period(MONDAY, MORNING, MONDAY, MORNING).days()).isEqualTo(days("0.5"));
    }

    @Test
    void shouldCountAfternoonToNextMorning() {
      assertThat(period(MONDAY, AFTERNOON, MONDAY.plusDays(1), MORNING).days()).isEqualTo(days("1"));
    }

    @Test
    void shouldNotCountWeekEnds() {
      assertThat(period(FRIDAY, MORNING, FRIDAY.plusDays(3), AFTERNOON).days()).isEqualTo(days("2"));
    }

    @Test
    void shouldCountWorkingWeek() {
      assertThat(period(MONDAY, MORNING, FRIDAY, AFTERNOON).days()).isEqualTo(days("5"));
    }
  }

  @Nested
  class Validation {

    @Test
    void shouldNotEndBeforeItStarts() {
      assertThatThrownBy(() -> period(FRIDAY, MORNING, MONDAY, AFTERNOON))
        .isExactlyInstanceOf(InvalidLeavePeriodException.class)
        .hasMessageContaining("ends before it starts");
    }

    @Test
    void shouldNotEndOnTheMorningOfADayStartedInTheAfternoon() {
      assertThatThrownBy(() -> period(MONDAY, AFTERNOON, MONDAY, MORNING)).isExactlyInstanceOf(InvalidLeavePeriodException.class);
    }

    @Test
    void shouldNotSpanTwoYears() {
      assertThatThrownBy(() -> period(LocalDate.of(2026, 12, 31), MORNING, LocalDate.of(2027, 1, 4), AFTERNOON))
        .isExactlyInstanceOf(InvalidLeavePeriodException.class)
        .hasMessageContaining("two years");
    }

    @Test
    void shouldNotStartOnWeekEnd() {
      assertThatThrownBy(() -> period(FRIDAY.plusDays(1), MORNING, FRIDAY.plusDays(3), AFTERNOON))
        .isExactlyInstanceOf(InvalidLeavePeriodException.class)
        .hasMessageContaining("working day");
    }

    @Test
    void shouldNotEndOnWeekEnd() {
      assertThatThrownBy(() -> period(FRIDAY, MORNING, FRIDAY.plusDays(2), AFTERNOON)).isExactlyInstanceOf(
        InvalidLeavePeriodException.class
      );
    }
  }

  @Test
  void shouldBelongToTheYearOfItsStart() {
    assertThat(period(MONDAY, MORNING, MONDAY, AFTERNOON).year()).isEqualTo(Year.of(2026));
  }

  @Test
  void shouldOverlapPeriodSharingAHalfDay() {
    var monday = period(MONDAY, MORNING, MONDAY, AFTERNOON);

    assertThat(monday.overlaps(period(MONDAY, AFTERNOON, FRIDAY, AFTERNOON))).isTrue();
    assertThat(period(MONDAY, AFTERNOON, FRIDAY, AFTERNOON).overlaps(monday)).isTrue();
  }

  @Test
  void shouldNotOverlapFollowingHalfDay() {
    assertThat(period(MONDAY, MORNING, MONDAY, MORNING).overlaps(period(MONDAY, AFTERNOON, MONDAY, AFTERNOON))).isFalse();
    assertThat(period(MONDAY, AFTERNOON, MONDAY, AFTERNOON).overlaps(period(MONDAY, MORNING, MONDAY, MORNING))).isFalse();
  }

  @Test
  void shouldContainItsHalfDaysOnly() {
    var period = period(MONDAY, AFTERNOON, FRIDAY, MORNING);

    assertThat(period.contains(new DayPart(MONDAY, AFTERNOON))).isTrue();
    assertThat(period.contains(new DayPart(MONDAY, MORNING))).isFalse();
    assertThat(period.contains(new DayPart(FRIDAY, AFTERNOON))).isFalse();
  }

  private static LeavePeriod period(LocalDate start, HalfDay startHalf, LocalDate end, HalfDay endHalf) {
    return new LeavePeriod(new DayPart(start, startHalf), new DayPart(end, endHalf));
  }

  private static LeaveDays days(String value) {
    return new LeaveDays(new BigDecimal(value));
  }
}
