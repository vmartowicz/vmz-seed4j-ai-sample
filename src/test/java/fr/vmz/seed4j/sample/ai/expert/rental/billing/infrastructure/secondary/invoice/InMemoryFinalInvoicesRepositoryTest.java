package fr.vmz.seed4j.sample.ai.expert.rental.billing.infrastructure.secondary.invoice;

import static fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.RentalBillingFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.FinalInvoice;
import fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.FinalInvoiceId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryFinalInvoicesRepositoryTest {

  private final InMemoryFinalInvoicesRepository invoices = new InMemoryFinalInvoicesRepository();

  @Test
  void shouldGetSavedInvoice() {
    var invoice = FinalInvoice.issue(onTime());

    invoices.save(invoice);

    assertThat(invoices.get(invoice.id())).containsSame(invoice);
    assertThat(invoices.get(FinalInvoiceId.newId())).isEmpty();
  }
}
