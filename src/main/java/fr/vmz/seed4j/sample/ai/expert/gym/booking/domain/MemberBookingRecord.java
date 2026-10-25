package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public record MemberBookingRecord(List<ClassBooking> bookings) {
  private static final int NO_SHOWS_BEFORE_BAN = 3;
  private static final int NO_SHOWS_WINDOW_DAYS = 30;
  private static final int BAN_DAYS = 7;

  public long bookedIn(YearMonth month) {
    return bookings
      .stream()
      .filter(ClassBooking::countsInQuota)
      .filter(booking -> YearMonth.from(booking.session().start()).equals(month))
      .count();
  }

  public boolean isBannedOn(LocalDate today) {
    var noShows = bookings
      .stream()
      .filter(ClassBooking::isNoShow)
      .map(booking -> booking.session().start().toLocalDate())
      .toList();

    return noShows
      .stream()
      .filter(noShow -> !today.isBefore(noShow) && today.isBefore(noShow.plusDays(BAN_DAYS)))
      .anyMatch(noShow -> noShowsInWindowEndingOn(noShows, noShow) >= NO_SHOWS_BEFORE_BAN);
  }

  private static long noShowsInWindowEndingOn(List<LocalDate> noShows, LocalDate end) {
    var windowStart = end.minusDays(NO_SHOWS_WINDOW_DAYS);

    return noShows
      .stream()
      .filter(noShow -> noShow.isAfter(windowStart) && !noShow.isAfter(end))
      .count();
  }
}
