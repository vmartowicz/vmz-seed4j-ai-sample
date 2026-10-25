package fr.vmz.seed4j.sample.ai.expert.gym.billing.infrastructure.secondary.invoice;

import static fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.BillingFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.InvoiceId;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryInvoicesRepositoryTest {

  private final InMemoryInvoicesRepository invoices = new InMemoryInvoicesRepository();

  @Test
  void shouldGetSavedInvoices() {
    var invoice = monthlyInvoice();
    invoices.save(invoice);
    invoices.save(monthlyInvoice());

    assertThat(invoices.get(invoice.id())).containsSame(invoice);
    assertThat(invoices.get(InvoiceId.newId())).isEmpty();
    assertThat(invoices.of(invoice.member())).containsExactly(invoice);
    assertThat(invoices.of(MemberId.newId())).isEmpty();
    assertThat(invoices.all()).hasSize(2);
  }
}
