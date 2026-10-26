package fr.vmz.seed4j.sample.ai.expert.rental.billing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.rental.billing.application.RentalBillingApplicationService;
import fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.FinalInvoiceId;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rental/billing")
class RentalBillingResource {

  private final RentalBillingApplicationService billing;

  RentalBillingResource(RentalBillingApplicationService billing) {
    this.billing = billing;
  }

  @GetMapping("/invoices/{invoiceId}")
  RestFinalInvoice invoice(@PathVariable UUID invoiceId) {
    return RestFinalInvoice.from(billing.invoice(new FinalInvoiceId(invoiceId)));
  }

  @PostMapping("/invoices/{invoiceId}/payment")
  RestFinalInvoice pay(@PathVariable UUID invoiceId) {
    return RestFinalInvoice.from(billing.pay(new FinalInvoiceId(invoiceId)));
  }

  @GetMapping("/deposits/{contractId}")
  RestDeposit deposit(@PathVariable UUID contractId) {
    return RestDeposit.from(billing.deposit(contractId));
  }

  @PostMapping("/deposits/{contractId}/damage-quote")
  RestDeposit retainForDamage(@PathVariable UUID contractId, @RequestBody RestDamageQuote quote) {
    return RestDeposit.from(billing.retainForDamage(contractId, quote.amount()));
  }
}
