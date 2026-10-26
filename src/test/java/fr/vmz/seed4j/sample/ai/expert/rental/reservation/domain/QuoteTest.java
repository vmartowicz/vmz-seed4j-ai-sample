package fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import org.junit.jupiter.api.Test;

@UnitTest
class QuoteTest {

  @Test
  void shouldPriceDaysOfCategory() {
    var quote = Quote.of(Category.SUV, 3, false, Options.NONE, false, false);

    assertThat(quote.days()).isEqualTo(3);
    assertThat(quote.dailyRate()).isEqualByComparingTo("69");
    assertThat(quote.rental()).isEqualByComparingTo("207");
    assertThat(quote.youngDriverFee()).isEqualByComparingTo("0");
    assertThat(quote.options()).isEqualByComparingTo("0");
    assertThat(quote.freeDay()).isEqualByComparingTo("0");
    assertThat(quote.total()).isEqualByComparingTo("207");
  }

  @Test
  void shouldAdd15EurosPerDayForYoungDrivers() {
    assertThat(Quote.of(Category.ECO, 3, true, Options.NONE, false, false).youngDriverFee()).isEqualByComparingTo("45");
  }

  @Test
  void shouldPriceOptionsPerDay() {
    var quote = Quote.of(Category.ECO, 3, false, new Options(true, true, true), false, false);

    assertThat(quote.options()).isEqualByComparingTo("69");
  }

  @Test
  void shouldCapGpsAt50Euros() {
    assertThat(Quote.of(Category.ECO, 7, false, new Options(false, false, true), false, false).options()).isEqualByComparingTo("50");
  }

  @Test
  void shouldOfferAdditionalDriverToGoldMembers() {
    assertThat(Quote.of(Category.ECO, 3, false, new Options(false, true, false), true, false).options()).isEqualByComparingTo("0");
  }

  @Test
  void shouldDeductFreeDay() {
    var quote = Quote.of(Category.COMFORT, 2, false, Options.NONE, false, true);

    assertThat(quote.freeDay()).isEqualByComparingTo("49");
    assertThat(quote.total()).isEqualByComparingTo("49");
  }
}
