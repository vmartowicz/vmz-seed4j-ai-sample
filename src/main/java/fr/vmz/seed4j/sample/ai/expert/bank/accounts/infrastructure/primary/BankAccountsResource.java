package fr.vmz.seed4j.sample.ai.expert.bank.accounts.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.accounts.application.RetailAccountsApplicationService;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.CardId;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
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
class BankAccountsResource {

  private final RetailAccountsApplicationService accounts;

  BankAccountsResource(RetailAccountsApplicationService accounts) {
    this.accounts = accounts;
  }

  @PostMapping("/accounts")
  @ResponseStatus(HttpStatus.CREATED)
  RestBankAccount open(@RequestBody RestAccountToOpen account) {
    return RestBankAccount.from(accounts.open(account.type(), account.holderIds()));
  }

  @GetMapping("/accounts/{iban}")
  RestBankAccount account(@PathVariable String iban) {
    return RestBankAccount.from(accounts.account(new Iban(iban)));
  }

  @PostMapping("/accounts/{iban}/deposits")
  RestBankAccount deposit(@PathVariable String iban, @RequestBody RestAmount deposit) {
    return RestBankAccount.from(accounts.deposit(new Iban(iban), deposit.amount()));
  }

  @PostMapping("/accounts/{iban}/cards")
  @ResponseStatus(HttpStatus.CREATED)
  RestCard issueCard(@PathVariable String iban, @RequestBody RestCardRequest card) {
    Assert.notNull("type", card.type());

    return RestCard.from(accounts.issueCard(new Iban(iban), card.type()));
  }

  @GetMapping("/cards/{cardId}")
  RestCard card(@PathVariable UUID cardId) {
    return RestCard.from(accounts.card(new CardId(cardId)));
  }

  @PostMapping("/cards/{cardId}/payments")
  RestCardAuthorization pay(@PathVariable UUID cardId, @RequestBody RestAmount payment) {
    Assert.notBlank("country", payment.country());

    return RestCardAuthorization.from(accounts.pay(new CardId(cardId), payment.amount(), payment.country()));
  }

  @PostMapping("/cards/{cardId}/withdrawals")
  RestCardAuthorization withdraw(@PathVariable UUID cardId, @RequestBody RestAmount withdrawal) {
    return RestCardAuthorization.from(accounts.withdraw(new CardId(cardId), withdrawal.amount()));
  }

  @PostMapping("/cards/{cardId}/wrong-pin")
  RestCard wrongPin(@PathVariable UUID cardId) {
    return RestCard.from(accounts.wrongPin(new CardId(cardId)));
  }

  @PostMapping("/cards/{cardId}/opposition")
  RestCard declareLostOrStolen(@PathVariable UUID cardId) {
    return RestCard.from(accounts.declareLostOrStolen(new CardId(cardId)));
  }
}
