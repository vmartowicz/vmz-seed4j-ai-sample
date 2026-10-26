package fr.vmz.seed4j.sample.ai.expert.bank.lending.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface LoanAccounts {
  Optional<Iban> currentAccountOf(CustomerId customer);

  boolean wasInUnauthorizedOverdraftSince(CustomerId customer, LocalDate date);

  void credit(Iban account, BigDecimal amount, String label);

  boolean collect(Iban account, BigDecimal amount, String label);
}
