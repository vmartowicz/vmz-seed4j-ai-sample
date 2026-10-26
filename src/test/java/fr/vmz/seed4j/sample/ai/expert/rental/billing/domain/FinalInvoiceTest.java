package fr.vmz.seed4j.sample.ai.expert.rental.billing.domain;

import static fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.RentalBillingFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@UnitTest
class FinalInvoiceTest {

  @Test
  void shouldSettleRentalReturnedOnTime() {
    var report = onTime();

    var invoice = FinalInvoice.issue(report);

    assertThat(invoice.id()).isNotNull();
    assertThat(invoice.contract()).isEqualTo(report.contract());
    assertThat(invoice.customer()).isEqualTo(JANE);
    assertThat(invoice.charges()).isEmpty();
    assertThat(invoice.total()).isEqualByComparingTo("0");
    assertThat(invoice.status()).isEqualTo(FinalInvoiceStatus.PAID);
    assertThat(invoice.loyaltyPoints()).isEqualTo(105);
  }

  @Nested
  class Late {

    @ParameterizedTest
    @CsvSource({ "59, 0", "60, 15", "70, 30", "120, 30", "121, 45", "180, 45" })
    void shouldBillStartedHoursAfterTolerance(int minutes, String fee) {
      var invoice = FinalInvoice.issue(returned(PLANNED_RETURN.plusMinutes(minutes), 600, RentalPoint.PART_DIEU));

      assertThat(amountOf(invoice, ChargeKind.LATE_HOURS)).isEqualByComparingTo(fee);
    }

    @Test
    void shouldBillExtraDaysBeyond3Hours() {
      var invoice = FinalInvoice.issue(returned(PLANNED_RETURN.plusMinutes(181), 600, RentalPoint.PART_DIEU));

      assertThat(amountOf(invoice, ChargeKind.LATE_HOURS)).isEqualByComparingTo("0");
      assertThat(amountOf(invoice, ChargeKind.LATE_DAYS)).isEqualByComparingTo("35");
    }

    @Test
    void shouldIncludeKilometresOfExtraDays() {
      var invoice = FinalInvoice.issue(returned(PLANNED_RETURN.plusHours(25), 1000, RentalPoint.PART_DIEU));

      assertThat(amountOf(invoice, ChargeKind.LATE_DAYS)).isEqualByComparingTo("70");
      assertThat(amountOf(invoice, ChargeKind.EXTRA_KILOMETRES)).isEqualByComparingTo("0");
    }
  }

  @Nested
  class Kilometres {

    @Test
    void shouldInclude200KmPerDay() {
      assertThat(amountOf(FinalInvoice.issue(returned(PLANNED_RETURN, 600, RentalPoint.PART_DIEU)), ChargeKind.EXTRA_KILOMETRES)).isEqualByComparingTo("0");
      assertThat(amountOf(FinalInvoice.issue(returned(PLANNED_RETURN, 641, RentalPoint.PART_DIEU)), ChargeKind.EXTRA_KILOMETRES)).isEqualByComparingTo(
        "10.25"
      );
    }

    @Test
    void shouldNotLimitPremiumKilometres() {
      var invoice = FinalInvoice.issue(report(Category.PREMIUM, PLANNED_RETURN, 5000, RentalPoint.PART_DIEU, BigDecimal.ZERO, false, false));

      assertThat(invoice.charges()).isEmpty();
    }
  }

  @Test
  void shouldBillMissingFuelOrEnergy() {
    var petrol = FinalInvoice.issue(report(Category.ECO, PLANNED_RETURN, 600, RentalPoint.PART_DIEU, new BigDecimal("10"), false, false));
    var electric = FinalInvoice.issue(report(Category.ECO, PLANNED_RETURN, 600, RentalPoint.PART_DIEU, new BigDecimal("10"), true, false));

    assertThat(amountOf(petrol, ChargeKind.FUEL)).isEqualByComparingTo("25");
    assertThat(amountOf(electric, ChargeKind.FUEL)).isEqualByComparingTo("4");
  }

  @Test
  void shouldBillOneWayReturnsExceptBetweenPartDieuAndVilleurbanne() {
    assertThat(amountOf(FinalInvoice.issue(returned(PLANNED_RETURN, 600, RentalPoint.SAINT_EXUPERY)), ChargeKind.ONE_WAY)).isEqualByComparingTo("50");
    assertThat(amountOf(FinalInvoice.issue(returned(PLANNED_RETURN, 600, RentalPoint.VILLEURBANNE)), ChargeKind.ONE_WAY)).isEqualByComparingTo("0");
  }

  @Test
  void shouldBillOneWayReturnsFromTheAirport() {
    var fromAirport = new ReturnReport(
      UUID.randomUUID(),
      JANE,
      Category.ECO,
      new BigDecimal("35"),
      3,
      new BigDecimal("105"),
      PICKED_UP,
      PLANNED_RETURN,
      PLANNED_RETURN,
      RentalPoint.SAINT_EXUPERY,
      RentalPoint.PART_DIEU,
      100,
      BigDecimal.ZERO,
      false,
      false
    );

    assertThat(amountOf(FinalInvoice.issue(fromAirport), ChargeKind.ONE_WAY)).isEqualByComparingTo("50");
  }

  @Test
  void shouldCreditUnusedDaysUpTo2() {
    var oneDayEarly = FinalInvoice.issue(returned(PLANNED_RETURN.minusDays(1), 300, RentalPoint.PART_DIEU));
    var cutShort = FinalInvoice.issue(returned(PICKED_UP.plusHours(2), 10, RentalPoint.PART_DIEU));
    var fewMinutesEarly = FinalInvoice.issue(returned(PLANNED_RETURN.minusMinutes(30), 300, RentalPoint.PART_DIEU));

    assertThat(amountOf(oneDayEarly, ChargeKind.EARLY_RETURN_CREDIT)).isEqualByComparingTo("-35");
    assertThat(amountOf(cutShort, ChargeKind.EARLY_RETURN_CREDIT)).isEqualByComparingTo("-70");
    assertThat(cutShort.total()).isEqualByComparingTo("-70");
    assertThat(cutShort.loyaltyPoints()).isEqualTo(35);
    assertThat(fewMinutesEarly.charges()).isEmpty();
  }

  @Test
  void shouldWaitForPaymentOfCharges() {
    var invoice = FinalInvoice.issue(report(Category.ECO, PLANNED_RETURN.plusMinutes(70), 641, RentalPoint.SAINT_EXUPERY, new BigDecimal("10"), false, false));

    assertThat(invoice.total()).isEqualByComparingTo("115.25");
    assertThat(invoice.status()).isEqualTo(FinalInvoiceStatus.ISSUED);
    assertThat(invoice.loyaltyPoints()).isEqualTo(195);

    invoice.pay();

    assertThat(invoice.status()).isEqualTo(FinalInvoiceStatus.PAID);
    assertThatThrownBy(invoice::pay).isExactlyInstanceOf(FinalInvoiceAlreadyPaidException.class);
  }

  private static BigDecimal amountOf(FinalInvoice invoice, ChargeKind kind) {
    return invoice
      .charges()
      .stream()
      .filter(charge -> charge.kind() == kind)
      .map(Charge::amount)
      .findFirst()
      .orElse(BigDecimal.ZERO);
  }
}
