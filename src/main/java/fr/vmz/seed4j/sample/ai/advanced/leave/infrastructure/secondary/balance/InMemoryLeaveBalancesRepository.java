package fr.vmz.seed4j.sample.ai.advanced.leave.infrastructure.secondary.balance;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.balance.LeaveBalance;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.balance.LeaveBalancesRepository;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeeId;
import java.time.Year;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryLeaveBalancesRepository implements LeaveBalancesRepository {

  private final Map<BalanceKey, LeaveBalance> balances = new ConcurrentHashMap<>();

  @Override
  public void save(LeaveBalance balance) {
    balances.put(new BalanceKey(balance.employee(), balance.year()), balance);
  }

  @Override
  public Optional<LeaveBalance> get(EmployeeId employee, Year year) {
    return Optional.ofNullable(balances.get(new BalanceKey(employee, year)));
  }

  private record BalanceKey(EmployeeId employee, Year year) {}
}
