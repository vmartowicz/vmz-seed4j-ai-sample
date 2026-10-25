package fr.vmz.seed4j.sample.ai.expert.gym.billing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.Invoice;
import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.InvoiceKind;
import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.InvoiceStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestInvoice(
  UUID id,
  InvoiceKind kind,
  String period,
  LocalDate issuedOn,
  BigDecimal amount,
  InvoiceStatus status,
  int attempts,
  @Nullable LocalDate nextAttemptOn
) {
  static RestInvoice from(Invoice invoice) {
    return new RestInvoice(
      invoice.id().get(),
      invoice.kind(),
      invoice.period().toString(),
      invoice.issuedOn(),
      invoice.amount().get(),
      invoice.status(),
      invoice.attempts(),
      invoice.nextAttemptOn().orElse(null)
    );
  }
}
