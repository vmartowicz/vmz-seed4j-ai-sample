package fr.vmz.seed4j.sample.ai.simple.bank.application;

import fr.vmz.seed4j.sample.ai.simple.bank.domain.Account;
import fr.vmz.seed4j.sample.ai.simple.bank.domain.AccountId;
import fr.vmz.seed4j.sample.ai.simple.bank.domain.AccountsRepository;
import fr.vmz.seed4j.sample.ai.simple.bank.domain.AccountsTeller;
import fr.vmz.seed4j.sample.ai.simple.bank.domain.Amount;
import fr.vmz.seed4j.sample.ai.simple.bank.domain.AuditTrail;
import fr.vmz.seed4j.sample.ai.simple.bank.domain.HolderName;
import fr.vmz.seed4j.sample.ai.simple.bank.domain.Label;
import fr.vmz.seed4j.sample.ai.simple.bank.domain.Operation;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AccountsApplicationService {

  private final AccountsRepository accounts;
  private final AccountsTeller teller;
  private final Clock clock;

  public AccountsApplicationService(AccountsRepository accounts, AuditTrail audit, Clock clock) {
    this.accounts = accounts;
    teller = new AccountsTeller(accounts, audit);
    this.clock = clock;
  }

  public Account open(HolderName holder) {
    return teller.open(holder, now());
  }

  public Account get(AccountId account) {
    return accounts.account(account);
  }

  public Operation receiveTransfer(AccountId account, Amount amount, Optional<Label> label) {
    return teller.receiveTransfer(account, amount, label, now());
  }

  public Operation pay(AccountId account, Amount amount, Optional<Label> label) {
    return teller.pay(account, amount, label, now());
  }

  public List<Operation> history(AccountId account) {
    return accounts.account(account).history();
  }

  private Instant now() {
    return clock.instant();
  }
}
