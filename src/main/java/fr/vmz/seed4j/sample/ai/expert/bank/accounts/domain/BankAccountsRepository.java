package fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import java.util.List;
import java.util.Optional;

public interface BankAccountsRepository {
  void save(BankAccount account);

  Optional<BankAccount> get(Iban iban);

  List<BankAccount> ofHolder(CustomerId customer);

  List<BankAccount> all();

  default BankAccount account(Iban iban) {
    return get(iban).orElseThrow(() -> new UnknownAccountException(iban));
  }
}
