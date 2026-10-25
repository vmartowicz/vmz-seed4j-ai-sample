package fr.vmz.seed4j.sample.ai.advanced.leave.domain.balance;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeeId;
import java.time.Year;
import java.util.Optional;

public interface LeaveBalancesRepository {
  void save(LeaveBalance balance);

  Optional<LeaveBalance> get(EmployeeId employee, Year year);
}
