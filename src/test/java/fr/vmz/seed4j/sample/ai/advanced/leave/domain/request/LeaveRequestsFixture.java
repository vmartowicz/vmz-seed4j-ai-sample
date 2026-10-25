package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.LeaveDays;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.Employee;
import java.math.BigDecimal;
import java.time.LocalDate;

public final class LeaveRequestsFixture {

  // Tuesday 2026-03-10
  public static final LocalDate TODAY = LocalDate.of(2026, 3, 10);
  // Monday 2026-04-06
  public static final LocalDate MONDAY = LocalDate.of(2026, 4, 6);

  private LeaveRequestsFixture() {}

  public static LeavePeriod fullDay(LocalDate date) {
    return fullDays(date, date);
  }

  public static LeavePeriod fullDays(LocalDate start, LocalDate end) {
    return new LeavePeriod(new DayPart(start, HalfDay.MORNING), new DayPart(end, HalfDay.AFTERNOON));
  }

  public static LeaveRequest pending(Employee employee, LeavePeriod period) {
    return new LeaveRequestToCreate(employee.id(), period, TODAY).create();
  }

  public static LeaveRequest approved(Employee employee, LeavePeriod period) {
    var request = pending(employee, period);
    request.approve();

    return request;
  }

  public static LeaveDays days(String value) {
    return new LeaveDays(new BigDecimal(value));
  }
}
