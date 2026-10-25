package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import static fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeesFixture.*;
import static fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeaveRequestsFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.LeaveAccessDeniedException;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.balance.LeaveBalance;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.balance.LeaveBalancesRepository;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeesRepository;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

@UnitTest
class EntitlementsManagerTest {

  private static final Year YEAR = Year.of(2026);

  private final EmployeesRepository employees = mock(EmployeesRepository.class, CALLS_REAL_METHODS);
  private final LeaveBalancesRepository balances = mock(LeaveBalancesRepository.class);
  private final LeaveRequestsRepository requests = mock(LeaveRequestsRepository.class, CALLS_REAL_METHODS);
  private final EntitlementsManager manager = new EntitlementsManager(employees, balances, requests);

  @BeforeEach
  void loadEmployees() {
    List.of(INES, EMMA, MARC).forEach(employee -> doReturn(Optional.of(employee)).when(employees).get(employee.id()));
    when(balances.get(any(), any())).thenReturn(Optional.empty());
    doReturn(List.of(approved(EMMA, fullDays(MONDAY, MONDAY.plusDays(4))))).when(requests).ofEmployee(EMMA.id());
  }

  @Test
  void shouldChangeEntitlementOfEmployee() {
    var summary = manager.changeEntitlement(INES.id(), EMMA.id(), YEAR, days("30"));

    var saved = ArgumentCaptor.forClass(LeaveBalance.class);
    verify(balances).save(saved.capture());
    assertThat(saved.getValue().entitlement()).isEqualTo(days("30"));
    assertThat(summary.remaining()).isEqualTo(days("25"));
  }

  @Test
  void shouldChangeEntitlementToConsumedDays() {
    assertThat(manager.changeEntitlement(INES.id(), EMMA.id(), YEAR, days("5")).remaining()).isEqualTo(days("0"));
  }

  @Test
  void shouldNotChangeEntitlementBelowConsumedDays() {
    assertThatThrownBy(() -> manager.changeEntitlement(INES.id(), EMMA.id(), YEAR, days("4.5")))
      .isExactlyInstanceOf(EntitlementBelowConsumedDaysException.class)
      .hasMessageContaining("5.0");
    verify(balances, never()).save(any());
  }

  @Test
  void shouldNotChangeEntitlementWithoutBeingHumanResources() {
    assertThatThrownBy(() -> manager.changeEntitlement(MARC.id(), EMMA.id(), YEAR, days("30"))).isExactlyInstanceOf(
      LeaveAccessDeniedException.class
    );
  }

  @Test
  void shouldGetBalanceSummary() {
    assertThat(manager.summaryOf(EMMA.id(), YEAR).approved()).isEqualTo(days("5"));
  }
}
