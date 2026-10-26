package fr.vmz.seed4j.sample.ai.expert.bank.accounts.infrastructure.secondary.account;

import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.BankAccount;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.BankAccountsRepository;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryBankAccountsRepository implements BankAccountsRepository {

  private final Map<Iban, BankAccount> accounts = new ConcurrentHashMap<>();

  @Override
  public void save(BankAccount account) {
    accounts.put(account.iban(), account);
  }

  @Override
  public Optional<BankAccount> get(Iban iban) {
    return Optional.ofNullable(accounts.get(iban));
  }

  @Override
  public List<BankAccount> ofHolder(CustomerId customer) {
    return accounts
      .values()
      .stream()
      .filter(account -> account.holders().contains(customer))
      .toList();
  }

  @Override
  public List<BankAccount> all() {
    return List.copyOf(accounts.values());
  }
}
