package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class PaymentsFixture {

  public static final LocalDateTime MONDAY_MORNING = LocalDateTime.of(2026, 6, 1, 10, 0);
  public static final Iban PAYER = Iban.generate();

  private PaymentsFixture() {}

  public static Payee payee() {
    return Payee.add(PAYER, "Jane Doe", Iban.generate(), MONDAY_MORNING.minusDays(10));
  }

  public static TransferToOrder order(Payee payee, String amount, TransferKind kind) {
    return new TransferToOrder(PAYER, payee.id(), new BigDecimal(amount), "Rent", kind);
  }

  public static BigDecimal euros(String amount) {
    return new BigDecimal(amount);
  }
}
