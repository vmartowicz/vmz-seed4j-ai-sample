package fr.vmz.seed4j.sample.ai.expert.bank.payments.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.payments.application.PaymentsApplicationService;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.DirectDebitId;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.MandateId;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.TransferId;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/retail-bank")
class PaymentsResource {

  private final PaymentsApplicationService payments;

  PaymentsResource(PaymentsApplicationService payments) {
    this.payments = payments;
  }

  @PostMapping("/accounts/{iban}/payees")
  @ResponseStatus(HttpStatus.CREATED)
  RestPayee addPayee(@PathVariable String iban, @RequestBody RestPayeeToAdd payee) {
    return RestPayee.from(payments.addPayee(new Iban(iban), payee.name(), new Iban(payee.iban())));
  }

  @PostMapping("/transfers")
  @ResponseStatus(HttpStatus.CREATED)
  RestTransfer order(@RequestBody RestTransferToOrder transfer) {
    return RestTransfer.from(payments.order(transfer.toDomain()));
  }

  @GetMapping("/transfers/{transferId}")
  RestTransfer transfer(@PathVariable UUID transferId) {
    return RestTransfer.from(payments.transfer(new TransferId(transferId)));
  }

  @PostMapping("/transfers/{transferId}/release")
  RestTransfer release(@PathVariable UUID transferId) {
    return RestTransfer.from(payments.release(new TransferId(transferId)));
  }

  @PostMapping("/accounts/{iban}/mandates")
  @ResponseStatus(HttpStatus.CREATED)
  RestMandate signMandate(@PathVariable String iban, @RequestBody RestMandateToSign mandate) {
    return RestMandate.from(payments.signMandate(new Iban(iban), mandate.creditor()));
  }

  @PostMapping("/mandates/{mandateId}/revocation")
  RestMandate revoke(@PathVariable UUID mandateId) {
    return RestMandate.from(payments.revoke(new MandateId(mandateId)));
  }

  @PostMapping("/mandates/{mandateId}/collections")
  @ResponseStatus(HttpStatus.CREATED)
  RestDirectDebit collect(@PathVariable UUID mandateId, @RequestBody RestCollection collection) {
    return RestDirectDebit.from(payments.collect(new MandateId(mandateId), collection.amount()));
  }

  @PostMapping("/direct-debits/{debitId}/refund")
  RestDirectDebit refund(@PathVariable UUID debitId) {
    return RestDirectDebit.from(payments.refund(new DirectDebitId(debitId)));
  }
}
