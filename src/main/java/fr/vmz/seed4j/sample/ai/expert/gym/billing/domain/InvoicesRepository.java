package fr.vmz.seed4j.sample.ai.expert.gym.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.util.List;
import java.util.Optional;

public interface InvoicesRepository {
  void save(Invoice invoice);

  Optional<Invoice> get(InvoiceId invoice);

  List<Invoice> of(MemberId member);

  List<Invoice> all();

  default Invoice invoice(InvoiceId invoice) {
    return get(invoice).orElseThrow(() -> new UnknownInvoiceException(invoice));
  }
}
