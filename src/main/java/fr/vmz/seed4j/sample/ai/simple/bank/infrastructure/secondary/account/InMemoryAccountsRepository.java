package fr.vmz.seed4j.sample.ai.simple.bank.infrastructure.secondary.account;

import fr.vmz.seed4j.sample.ai.simple.bank.domain.Account;
import fr.vmz.seed4j.sample.ai.simple.bank.domain.AccountId;
import fr.vmz.seed4j.sample.ai.simple.bank.domain.AccountsRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryAccountsRepository implements AccountsRepository {

  private final Map<AccountId, Account> accounts = new ConcurrentHashMap<>();

  @Override
  public void save(Account account) {
    accounts.put(account.id(), account);
  }

  @Override
  public Optional<Account> get(AccountId account) {
    return Optional.ofNullable(accounts.get(account));
  }
}
