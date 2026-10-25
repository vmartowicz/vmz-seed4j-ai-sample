package fr.vmz.seed4j.sample.ai.expert.gym.billing.infrastructure.secondary.invoice;

import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.Invoice;
import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.InvoiceId;
import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.InvoicesRepository;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryInvoicesRepository implements InvoicesRepository {

  private final Map<InvoiceId, Invoice> invoices = new ConcurrentHashMap<>();

  @Override
  public void save(Invoice invoice) {
    invoices.put(invoice.id(), invoice);
  }

  @Override
  public Optional<Invoice> get(InvoiceId invoice) {
    return Optional.ofNullable(invoices.get(invoice));
  }

  @Override
  public List<Invoice> of(MemberId member) {
    return invoices
      .values()
      .stream()
      .filter(invoice -> invoice.member().equals(member))
      .toList();
  }

  @Override
  public List<Invoice> all() {
    return List.copyOf(invoices.values());
  }
}
