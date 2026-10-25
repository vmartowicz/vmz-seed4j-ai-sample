package fr.vmz.seed4j.sample.ai.advanced.leave.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.leave.application.LeaveBalancesApplicationService;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeeId;
import java.time.Year;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leave")
class LeaveBalancesResource {

  private static final String EMPLOYEE_HEADER = "X-Employee-Id";

  private final LeaveBalancesApplicationService balances;

  LeaveBalancesResource(LeaveBalancesApplicationService balances) {
    this.balances = balances;
  }

  @GetMapping("/balance")
  RestBalance balance(@RequestHeader(EMPLOYEE_HEADER) UUID employee, @RequestParam(required = false) @Nullable Integer year) {
    return RestBalance.from(balances.balanceOf(new EmployeeId(employee), Optional.ofNullable(year).map(Year::of)));
  }

  @PutMapping("/balances/{employeeId}/{year}")
  RestBalance changeEntitlement(
    @RequestHeader(EMPLOYEE_HEADER) UUID humanResources,
    @PathVariable UUID employeeId,
    @PathVariable int year,
    @RequestBody RestEntitlement entitlement
  ) {
    return RestBalance.from(
      balances.changeEntitlement(new EmployeeId(humanResources), new EmployeeId(employeeId), Year.of(year), entitlement.toDomain())
    );
  }
}
