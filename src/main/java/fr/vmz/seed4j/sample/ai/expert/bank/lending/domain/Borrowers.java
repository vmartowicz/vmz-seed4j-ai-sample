package fr.vmz.seed4j.sample.ai.expert.bank.lending.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import java.util.Optional;

public interface Borrowers {
  Optional<Borrower> borrower(CustomerId customer);
}
