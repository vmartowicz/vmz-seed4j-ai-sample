package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import static fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeesFixture.*;
import static fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeaveRequestsFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.balance.LeaveBalance;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.balance.LeaveBalancesRepository;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

@UnitTest
class BalanceCalculatorTest {

  private static final Year YEAR = Year.of(2026);

  private final LeaveBalancesRepository balances = mock(LeaveBalancesRepository.class);
  private final LeaveRequestsRepository requests = mock(LeaveRequestsRepository.class, CALLS_REAL_METHODS);
  private final BalanceCalculator calculator = new BalanceCalculator(balances, requests);

  @Test
  void shouldStartWith25DaysRemaining() {
    when(balances.get(any(), any())).thenReturn(Optional.empty());
    doReturn(List.of()).when(requests).ofEmployee(EMMA.id());

    var summary = calculator.summaryOf(EMMA.id(), YEAR);

    assertThat(summary.year()).isEqualTo(YEAR);
    assertThat(summary.entitlement()).isEqualTo(days("25"));
    assertThat(summary.approved()).isEqualTo(days("0"));
    assertThat(summary.pending()).isEqualTo(days("0"));
    assertThat(summary.remaining()).isEqualTo(days("25"));
  }

  @Test
  void shouldDeductApprovedAndPendingLeavesOfTheYear() {
    var balance = LeaveBalance.standard(EMMA.id(), YEAR);
    balance.changeEntitlement(days("20"));
    when(balances.get(EMMA.id(), YEAR)).thenReturn(Optional.of(balance));
    var rejected = pending(EMMA, fullDay(MONDAY.plusDays(14)));
    rejected.reject(Optional.empty());
    doReturn(
      List.of(
        approved(EMMA, fullDays(MONDAY, MONDAY.plusDays(4))),
        pending(EMMA, fullDay(MONDAY.plusDays(7))),
        rejected,
        approved(EMMA, fullDay(LocalDate.of(2027, 1, 4)))
      )
    )
      .when(requests)
      .ofEmployee(EMMA.id());

    var summary = calculator.summaryOf(EMMA.id(), YEAR);

    assertThat(summary.entitlement()).isEqualTo(days("20"));
    assertThat(summary.approved()).isEqualTo(days("5"));
    assertThat(summary.pending()).isEqualTo(days("1"));
    assertThat(summary.consumed()).isEqualTo(days("6"));
    assertThat(summary.remaining()).isEqualTo(days("14"));
  }
}
