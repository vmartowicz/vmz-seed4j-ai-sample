package fr.vmz.seed4j.sample.ai.advanced.leave.application;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.LeaveDays;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.balance.LeaveBalancesRepository;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeeId;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeesRepository;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.BalanceSummary;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.EntitlementsManager;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeaveRequestsRepository;
import java.time.Clock;
import java.time.Year;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class LeaveBalancesApplicationService {

  private final EntitlementsManager entitlements;
  private final Clock clock;

  public LeaveBalancesApplicationService(
    EmployeesRepository employees,
    LeaveBalancesRepository balances,
    LeaveRequestsRepository requests,
    Clock clock
  ) {
    entitlements = new EntitlementsManager(employees, balances, requests);
    this.clock = clock;
  }

  public BalanceSummary balanceOf(EmployeeId employee, Optional<Year> year) {
    return entitlements.summaryOf(employee, year.orElseGet(() -> Year.now(clock)));
  }

  public BalanceSummary changeEntitlement(EmployeeId humanResources, EmployeeId employee, Year year, LeaveDays entitlement) {
    return entitlements.changeEntitlement(humanResources, employee, year, entitlement);
  }
}
