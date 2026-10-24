package fr.vmz.seed4j.sample.ai.simple.bank.domain;

import java.util.Optional;

public interface AccountsRepository {
  void save(Account account);

  Optional<Account> get(AccountId account);

  default Account account(AccountId account) {
    return get(account).orElseThrow(() -> new UnknownAccountException(account));
  }
}
