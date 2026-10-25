package fr.vmz.seed4j.sample.ai.expert.gym.billing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.billing.application.BillingApplicationService;
import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.InvoiceId;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gym/billing")
class BillingResource {

  private final BillingApplicationService billing;

  BillingResource(BillingApplicationService billing) {
    this.billing = billing;
  }

  @GetMapping("/accounts/{memberId}")
  RestAccountStatement statement(@PathVariable UUID memberId) {
    return RestAccountStatement.from(billing.statement(new MemberId(memberId)));
  }

  @PostMapping("/accounts/{memberId}/settlement")
  RestAccountStatement settle(@PathVariable UUID memberId) {
    return RestAccountStatement.from(billing.settle(new MemberId(memberId)));
  }

  @PostMapping("/invoices/{invoiceId}/card-payment")
  RestInvoice payByCard(@PathVariable UUID invoiceId) {
    return RestInvoice.from(billing.payByCard(new InvoiceId(invoiceId)));
  }

  @PostMapping("/invoices/{invoiceId}/debit-result")
  RestInvoice debitResult(@PathVariable UUID invoiceId, @RequestBody RestDebitResult result) {
    return RestInvoice.from(billing.debitResult(new InvoiceId(invoiceId), result.accepted()));
  }
}
