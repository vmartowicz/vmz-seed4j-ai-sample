package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

import static fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.ClassBookingsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import org.junit.jupiter.api.Test;

@UnitTest
class MemberBookingRecordTest {

  private final MemberId member = MemberId.newId();

  @Test
  void shouldCountBookingsOfMonthInQuota() {
    var march = booked(member, sessionAt(LocalDateTime.of(2026, 3, 31, 20, 0), 10));
    var april = booked(member, sessionAt(LocalDateTime.of(2026, 4, 1, 8, 0), 10));
    var freeCancellation = booked(member, session(10));
    freeCancellation.cancel(NOW);
    var late = noShowAt(member, LocalDateTime.of(2026, 3, 2, 9, 0));

    var record = new MemberBookingRecord(List.of(march, april, freeCancellation, late));

    assertThat(record.bookedIn(YearMonth.of(2026, 3))).isEqualTo(2);
  }

  @Test
  void shouldNotBanAfter2NoShows() {
    var record = new MemberBookingRecord(List.of(noShow(1), noShow(10)));

    assertThat(record.isBannedOn(day(10))).isFalse();
  }

  @Test
  void shouldBanFor7DaysAfterThirdNoShowIn30Days() {
    var record = new MemberBookingRecord(List.of(noShow(20), noShow(1), noShow(10)));

    assertThat(record.isBannedOn(day(19))).isFalse();
    assertThat(record.isBannedOn(day(20))).isTrue();
    assertThat(record.isBannedOn(day(26))).isTrue();
    assertThat(record.isBannedOn(day(27))).isFalse();
  }

  @Test
  void shouldNotBanForNoShowsSpreadOverMoreThan30Days() {
    var record = new MemberBookingRecord(List.of(noShow(1), noShow(10), noShow(31)));

    assertThat(record.isBannedOn(LocalDate.of(2026, 1, 31))).isFalse();
  }

  @Test
  void shouldCountOnlyPreviousNoShowsOfEachBan() {
    var record = new MemberBookingRecord(List.of(noShow(5), noShow(6), noShow(7)));

    assertThat(record.isBannedOn(day(6))).isFalse();
    assertThat(record.isBannedOn(day(7))).isTrue();
  }

  @Test
  void shouldCountLateCancellationsAsNoShows() {
    var late = booked(member, sessionAt(LocalDateTime.of(2026, 1, 20, 18, 0), 10));
    late.cancel(LocalDateTime.of(2026, 1, 20, 17, 0));

    var record = new MemberBookingRecord(List.of(noShow(1), noShow(10), late));

    assertThat(record.isBannedOn(day(20))).isTrue();
  }

  private ClassBooking noShow(int dayOfJanuary) {
    return noShowAt(member, day(dayOfJanuary).atTime(18, 0));
  }

  private static LocalDate day(int dayOfJanuary) {
    return LocalDate.of(2026, 1, dayOfJanuary);
  }
}
