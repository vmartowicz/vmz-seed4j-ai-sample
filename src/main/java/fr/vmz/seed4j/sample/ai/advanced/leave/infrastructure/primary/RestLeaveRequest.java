package fr.vmz.seed4j.sample.ai.advanced.leave.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeaveRequest;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.RejectionComment;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestLeaveRequest(
  UUID id,
  UUID employeeId,
  LocalDate startDate,
  RestHalfDay startHalf,
  LocalDate endDate,
  RestHalfDay endHalf,
  BigDecimal days,
  String status,
  boolean shortNotice,
  @Nullable String rejectionComment
) {
  static RestLeaveRequest from(LeaveRequest request) {
    var period = request.period();

    return new RestLeaveRequest(
      request.id().get(),
      request.employee().get(),
      period.start().date(),
      RestHalfDay.from(period.start().half()),
      period.end().date(),
      RestHalfDay.from(period.end().half()),
      period.days().get(),
      request.status().name(),
      request.shortNotice(),
      request.rejectionComment().map(RejectionComment::get).orElse(null)
    );
  }
}
