package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import static fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeesFixture.*;
import static fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeaveRequestsFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.LeaveAccessDeniedException;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.balance.LeaveBalancesRepository;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeeId;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeesRepository;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.UnknownEmployeeException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class LeaveRequesterTest {

  private final EmployeesRepository employees = mock(EmployeesRepository.class, CALLS_REAL_METHODS);
  private final LeaveBalancesRepository balances = mock(LeaveBalancesRepository.class);
  private final LeaveRequestsRepository requests = mock(LeaveRequestsRepository.class, CALLS_REAL_METHODS);
  private final LeaveRequester requester = new LeaveRequester(employees, balances, requests);

  @BeforeEach
  void loadEmployees() {
    doReturn(Optional.of(EMMA)).when(employees).get(EMMA.id());
    when(balances.get(any(), any())).thenReturn(Optional.empty());
    doReturn(List.of()).when(requests).ofEmployee(any());
  }

  @Nested
  class Request {

    @Test
    void shouldCreatePendingRequest() {
      var request = requester.request(EMMA.id(), fullDay(MONDAY), TODAY);

      assertThat(request.status()).isEqualTo(LeaveRequestStatus.PENDING);
      assertThat(request.shortNotice()).isFalse();
      verify(requests).save(request);
    }

    @Test
    void shouldRequestLeaveStartingToday() {
      assertThat(requester.request(EMMA.id(), fullDay(TODAY), TODAY).shortNotice()).isTrue();
    }

    @Test
    void shouldNotRequestLeaveInThePast() {
      assertThatThrownBy(() -> requester.request(EMMA.id(), fullDay(TODAY.minusDays(1)), TODAY))
        .isExactlyInstanceOf(LeaveInThePastException.class)
        .hasMessageContaining("2026-03-09");
      verify(requests, never()).save(any());
    }

    @Test
    void shouldNotOverlapAnActiveLeave() {
      doReturn(List.of(pending(EMMA, fullDays(MONDAY, MONDAY.plusDays(2))))).when(requests).ofEmployee(EMMA.id());

      assertThatThrownBy(() -> requester.request(EMMA.id(), fullDay(MONDAY.plusDays(2)), TODAY)).isExactlyInstanceOf(
        OverlappingLeaveException.class
      );
    }

    @Test
    void shouldOverlapACancelledLeave() {
      var cancelled = pending(EMMA, fullDay(MONDAY));
      cancelled.cancel();
      doReturn(List.of(cancelled)).when(requests).ofEmployee(EMMA.id());

      assertThat(requester.request(EMMA.id(), fullDay(MONDAY), TODAY).status()).isEqualTo(LeaveRequestStatus.PENDING);
    }

    @Test
    void shouldUseWholeRemainingBalance() {
      doReturn(List.of(approved(EMMA, fullDays(MONDAY.plusDays(28), MONDAY.plusDays(59))))).when(requests).ofEmployee(EMMA.id());

      assertThat(requester.request(EMMA.id(), fullDay(MONDAY), TODAY).period().days()).isEqualTo(days("1"));
    }

    @Test
    void shouldNotExceedRemainingBalance() {
      doReturn(List.of(approved(EMMA, fullDays(MONDAY.plusDays(28), MONDAY.plusDays(59))))).when(requests).ofEmployee(EMMA.id());

      assertThatThrownBy(() -> requester.request(EMMA.id(), fullDays(MONDAY, MONDAY.plusDays(1)), TODAY))
        .isExactlyInstanceOf(InsufficientLeaveBalanceException.class)
        .hasMessageContaining("1.0");
      verify(requests, never()).save(any());
    }

    @Test
    void shouldNotRequestForUnknownEmployee() {
      var unknown = EmployeeId.newId();
      doReturn(Optional.empty()).when(employees).get(unknown);

      assertThatThrownBy(() -> requester.request(unknown, fullDay(MONDAY), TODAY))
        .isExactlyInstanceOf(UnknownEmployeeException.class)
        .hasMessageContaining(unknown.get().toString());
    }
  }

  @Nested
  class Cancel {

    @Test
    void shouldCancelOwnRequest() {
      var request = existing(pending(EMMA, fullDay(MONDAY)));

      requester.cancel(EMMA.id(), request.id());

      assertThat(request.status()).isEqualTo(LeaveRequestStatus.CANCELLED);
      verify(requests).save(request);
    }

    @Test
    void shouldNotCancelRequestOfAnotherEmployee() {
      var request = existing(pending(LUCAS, fullDay(MONDAY)));

      assertThatThrownBy(() -> requester.cancel(EMMA.id(), request.id())).isExactlyInstanceOf(LeaveAccessDeniedException.class);
      assertThat(request.status()).isEqualTo(LeaveRequestStatus.PENDING);
    }

    @Test
    void shouldNotCancelUnknownRequest() {
      var unknown = LeaveRequestId.newId();
      doReturn(Optional.empty()).when(requests).get(unknown);

      assertThatThrownBy(() -> requester.cancel(EMMA.id(), unknown))
        .isExactlyInstanceOf(UnknownLeaveRequestException.class)
        .hasMessageContaining(unknown.get().toString());
    }
  }

  @Test
  void shouldListOwnRequestsFromLatest() {
    var april = pending(EMMA, fullDay(MONDAY));
    var may = pending(EMMA, fullDay(MONDAY.plusDays(28)));
    doReturn(List.of(april, may)).when(requests).ofEmployee(EMMA.id());

    assertThat(requester.requestsOf(EMMA.id())).containsExactly(may, april);
  }

  private LeaveRequest existing(LeaveRequest request) {
    doReturn(Optional.of(request)).when(requests).get(request.id());

    return request;
  }
}
