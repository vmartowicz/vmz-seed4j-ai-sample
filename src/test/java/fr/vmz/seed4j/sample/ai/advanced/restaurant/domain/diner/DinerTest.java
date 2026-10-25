package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.time.LocalDate;
import java.time.Year;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

@UnitTest
class DinerTest {

  private static final LocalDate TODAY = LocalDate.of(2026, 3, 10);

  @Test
  void shouldWelcomeNewcomerAsRegular() {
    var diner = Diner.newcomer(new DinerEmail("jane@mail.com"));

    assertThat(diner.email()).isEqualTo(new DinerEmail("jane@mail.com"));
    assertThat(diner.honoredBookings()).isZero();
    assertThat(diner.noShows()).isZero();
    assertThat(diner.status()).isEqualTo(DinerStatus.REGULAR);
    assertThat(diner.isLoyal()).isFalse();
  }

  @Test
  void shouldStayRegularWith4HonoredBookings() {
    var diner = honored(4);

    assertThat(diner.status()).isEqualTo(DinerStatus.REGULAR);
  }

  @Test
  void shouldBecomeLoyalWith5HonoredBookings() {
    var diner = honored(5);

    assertThat(diner.honoredBookings()).isEqualTo(5);
    assertThat(diner.status()).isEqualTo(DinerStatus.LOYAL);
    assertThat(diner.isLoyal()).isTrue();
  }

  @Test
  void shouldStayAllowedWith2NoShows() {
    var diner = jane();
    diner.noShow();
    diner.noShow();

    assertThat(diner.noShows()).isEqualTo(2);
    assertThatCode(() -> diner.assertCanBook(TODAY)).doesNotThrowAnyException();
  }

  @Test
  void shouldBlacklistAfter3NoShowsEvenWhenLoyal() {
    var diner = honored(5);
    diner.noShow();
    diner.noShow();
    diner.noShow();

    assertThat(diner.status()).isEqualTo(DinerStatus.BLACKLISTED);
    assertThat(diner.isLoyal()).isFalse();
    assertThatThrownBy(() -> diner.assertCanBook(TODAY)).isExactlyInstanceOf(BlacklistedDinerException.class).hasMessageContaining(
      "jane@mail.com"
    );
  }

  @Test
  void shouldCountCancellationsByCalendarYear() {
    var diner = jane();
    diner.cancel(LocalDate.of(2025, 12, 31));
    diner.cancel(LocalDate.of(2026, 1, 1));

    assertThat(diner.cancellationsIn(Year.of(2025))).isEqualTo(1);
    assertThat(diner.cancellationsIn(Year.of(2026))).isEqualTo(1);
  }

  @Test
  void shouldAllowBookingWith2CancellationsThisYear() {
    var diner = jane();
    diner.cancel(TODAY);
    diner.cancel(TODAY);

    assertThatCode(() -> diner.assertCanBook(TODAY)).doesNotThrowAnyException();
  }

  @Test
  void shouldNotAllowBookingAfter3CancellationsThisYear() {
    var diner = jane();
    diner.cancel(TODAY);
    diner.cancel(TODAY);
    diner.cancel(TODAY);

    assertThatThrownBy(() -> diner.assertCanBook(TODAY)).isExactlyInstanceOf(CancellationLimitReachedException.class).hasMessageContaining(
      "2026"
    );
  }

  @Test
  void shouldAllowBookingAgainTheNextYear() {
    var diner = jane();
    diner.cancel(TODAY);
    diner.cancel(TODAY);
    diner.cancel(TODAY);

    assertThatCode(() -> diner.assertCanBook(LocalDate.of(2027, 1, 1))).doesNotThrowAnyException();
  }

  private static Diner jane() {
    return Diner.newcomer(new DinerEmail("jane@mail.com"));
  }

  private static Diner honored(int bookings) {
    var diner = jane();
    IntStream.range(0, bookings).forEach(booking -> diner.honor());

    return diner;
  }
}
