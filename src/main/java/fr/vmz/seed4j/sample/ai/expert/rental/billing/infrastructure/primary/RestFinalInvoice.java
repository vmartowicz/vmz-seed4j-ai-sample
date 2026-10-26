package fr.vmz.seed4j.sample.ai.expert.rental.billing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.FinalInvoice;
import fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.FinalInvoiceStatus;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

record RestFinalInvoice(UUID id, UUID contractId, String customer, List<RestCharge> charges, BigDecimal total, FinalInvoiceStatus status, int loyaltyPoints) {
  static RestFinalInvoice from(FinalInvoice invoice) {
    return new RestFinalInvoice(
      invoice.id().get(),
      invoice.contract(),
      invoice.customer().get(),
      invoice.charges().stream().map(RestCharge::from).toList(),
      invoice.total(),
      invoice.status(),
      invoice.loyaltyPoints()
    );
  }
}
