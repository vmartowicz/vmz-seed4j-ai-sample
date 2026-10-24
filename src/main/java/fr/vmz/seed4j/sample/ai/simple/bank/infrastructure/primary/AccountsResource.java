package fr.vmz.seed4j.sample.ai.simple.bank.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.simple.bank.application.AccountsApplicationService;
import fr.vmz.seed4j.sample.ai.simple.bank.domain.AccountId;
import java.util.List;
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
@RequestMapping("/api/bank/accounts")
class AccountsResource {

  private final AccountsApplicationService accounts;

  AccountsResource(AccountsApplicationService accounts) {
    this.accounts = accounts;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  RestAccount open(@RequestBody RestAccountToOpen account) {
    return RestAccount.from(accounts.open(account.holder()));
  }

  @GetMapping("/{accountId}")
  RestAccount get(@PathVariable UUID accountId) {
    return RestAccount.from(accounts.get(new AccountId(accountId)));
  }

  @PostMapping("/{accountId}/transfers")
  @ResponseStatus(HttpStatus.CREATED)
  RestOperation receiveTransfer(@PathVariable UUID accountId, @RequestBody RestMoneyMovement transfer) {
    return RestOperation.from(accounts.receiveTransfer(new AccountId(accountId), transfer.toAmount(), transfer.toLabel()));
  }

  @PostMapping("/{accountId}/payments")
  @ResponseStatus(HttpStatus.CREATED)
  RestOperation pay(@PathVariable UUID accountId, @RequestBody RestMoneyMovement payment) {
    return RestOperation.from(accounts.pay(new AccountId(accountId), payment.toAmount(), payment.toLabel()));
  }

  @GetMapping("/{accountId}/operations")
  List<RestOperation> history(@PathVariable UUID accountId) {
    return accounts.history(new AccountId(accountId)).stream().map(RestOperation::from).toList();
  }
}
