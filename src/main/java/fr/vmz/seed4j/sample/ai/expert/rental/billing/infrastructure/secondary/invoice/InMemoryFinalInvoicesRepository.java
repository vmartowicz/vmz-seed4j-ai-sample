package fr.vmz.seed4j.sample.ai.expert.rental.billing.infrastructure.secondary.invoice;

import fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.FinalInvoice;
import fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.FinalInvoiceId;
import fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.FinalInvoicesRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryFinalInvoicesRepository implements FinalInvoicesRepository {

  private final Map<FinalInvoiceId, FinalInvoice> invoices = new ConcurrentHashMap<>();

  @Override
  public void save(FinalInvoice invoice) {
    invoices.put(invoice.id(), invoice);
  }

  @Override
  public Optional<FinalInvoice> get(FinalInvoiceId invoice) {
    return Optional.ofNullable(invoices.get(invoice));
  }
}
