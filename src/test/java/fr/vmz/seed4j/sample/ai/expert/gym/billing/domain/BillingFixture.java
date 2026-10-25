package fr.vmz.seed4j.sample.ai.expert.gym.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public final class BillingFixture {

  public static final LocalDate SUBSCRIPTION_DAY = LocalDate.of(2026, 3, 10);
  public static final LocalDate DEBIT_DAY = LocalDate.of(2026, 4, 5);
  public static final YearMonth APRIL = YearMonth.of(2026, 4);

  private BillingFixture() {}

  public static Money euros(String amount) {
    return new Money(new BigDecimal(amount));
  }

  public static Invoice monthlyInvoice() {
    return Invoice.monthly(MemberId.newId(), euros("29.90"), APRIL, DEBIT_DAY);
  }

  public static Invoice rejectedTimes(int rejections) {
    return rejectedTimes(MemberId.newId(), rejections);
  }

  public static Invoice rejectedTimes(MemberId member, int rejections) {
    var invoice = Invoice.monthly(member, euros("29.90"), APRIL, DEBIT_DAY);
    var day = DEBIT_DAY;
    for (int rejection = 0; rejection < rejections; rejection++) {
      invoice.debitRejected(day);
      day = invoice.nextAttemptOn().orElse(day);
      invoice.retryIfDue(day);
    }

    return invoice;
  }
}
