package fr.vmz.seed4j.sample.ai.expert.rental.billing.domain;

import java.util.Optional;

public interface FinalInvoicesRepository {
  void save(FinalInvoice invoice);

  Optional<FinalInvoice> get(FinalInvoiceId invoice);

  default FinalInvoice invoice(FinalInvoiceId invoice) {
    return get(invoice).orElseThrow(() -> new UnknownFinalInvoiceException(invoice));
  }
}
