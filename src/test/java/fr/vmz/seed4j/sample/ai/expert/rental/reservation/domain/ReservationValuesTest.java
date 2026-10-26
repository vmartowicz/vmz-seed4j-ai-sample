package fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain;

import static fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.ReservationsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@UnitTest
class ReservationValuesTest {

  @ParameterizedTest
  @CsvSource({ "1, 1", "1440, 1", "1441, 2", "4320, 3" })
  void shouldCountStartedDays(int minutes, int days) {
    var period = new RentalPeriod(RentalPoint.PART_DIEU, PICK_UP, RentalPoint.VILLEURBANNE, PICK_UP.plusMinutes(minutes));

    assertThat(period.days()).isEqualTo(days);
  }

  @Test
  void shouldNotReturnBeforePickUp() {
    assertThatThrownBy(() -> new RentalPeriod(RentalPoint.PART_DIEU, PICK_UP, RentalPoint.PART_DIEU, PICK_UP)).isExactlyInstanceOf(
      InvalidRentalPeriodException.class
    );
  }

  @Test
  void shouldNotBuildPeriodWithoutPoint() {
    assertThatThrownBy(() -> new RentalPeriod(null, PICK_UP, RentalPoint.PART_DIEU, PICK_UP.plusDays(1))).isInstanceOf(AssertionException.class);
  }

  @Test
  void shouldPriceEachCategory() {
    assertThat(DailyRates.of(Category.ECO)).isEqualByComparingTo("35");
    assertThat(DailyRates.of(Category.COMFORT)).isEqualByComparingTo("49");
    assertThat(DailyRates.of(Category.SUV)).isEqualByComparingTo("69");
    assertThat(DailyRates.of(Category.VAN)).isEqualByComparingTo("79");
    assertThat(DailyRates.of(Category.PREMIUM)).isEqualByComparingTo("119");
  }

  @Test
  void shouldNeedDriversOf21WithLicenceFor3Years() {
    var day = LocalDate.of(2026, 6, 10);
    var justOldEnough = new Driver("Jim", new Email("jim@mail.com"), day.minusYears(21), day.minusYears(3));

    assertThatCode(() -> justOldEnough.assertCanDriveOn(day)).doesNotThrowAnyException();
    assertThat(justOldEnough.isYoungOn(day)).isTrue();
    assertThat(jane().isYoungOn(day)).isFalse();
  }

  @Test
  void shouldNotAcceptDriversUnder21() {
    var day = LocalDate.of(2026, 6, 10);
    var tooYoung = new Driver("Jim", new Email("jim@mail.com"), day.minusYears(21).plusDays(1), day.minusYears(3));

    assertThatThrownBy(() -> tooYoung.assertCanDriveOn(day)).isExactlyInstanceOf(DriverNotEligibleException.class).hasMessageContaining("21");
  }

  @Test
  void shouldNotAcceptRecentLicences() {
    var day = LocalDate.of(2026, 6, 10);
    var newDriver = new Driver("Jim", new Email("jim@mail.com"), day.minusYears(30), day.minusYears(3).plusDays(1));

    assertThatThrownBy(() -> newDriver.assertCanDriveOn(day)).isExactlyInstanceOf(DriverNotEligibleException.class).hasMessageContaining("3 years");
  }

  @Test
  void shouldNotBuildDriverWithoutName() {
    assertThatThrownBy(() -> new Driver(" ", new Email("jim@mail.com"), LocalDate.of(1990, 1, 1), LocalDate.of(2010, 1, 1))).isInstanceOf(
      AssertionException.class
    );
  }
}
