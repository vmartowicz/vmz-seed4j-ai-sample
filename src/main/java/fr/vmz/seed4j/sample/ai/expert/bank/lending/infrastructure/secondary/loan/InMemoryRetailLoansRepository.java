package fr.vmz.seed4j.sample.ai.expert.bank.lending.infrastructure.secondary.loan;

import fr.vmz.seed4j.sample.ai.expert.bank.lending.domain.Loan;
import fr.vmz.seed4j.sample.ai.expert.bank.lending.domain.LoanId;
import fr.vmz.seed4j.sample.ai.expert.bank.lending.domain.LoansRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryRetailLoansRepository implements LoansRepository {

  private final Map<LoanId, Loan> loans = new ConcurrentHashMap<>();

  @Override
  public void save(Loan loan) {
    loans.put(loan.id(), loan);
  }

  @Override
  public Optional<Loan> get(LoanId loan) {
    return Optional.ofNullable(loans.get(loan));
  }

  @Override
  public List<Loan> all() {
    return List.copyOf(loans.values());
  }
}
