package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.LeaveRuleViolationException;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.TeamName;

class TeamLimitReachedException extends LeaveRuleViolationException {

  TeamLimitReachedException(TeamName team, DayPart halfDay) {
    super("Team " + team.get() + " already has 3 people on leave on " + halfDay.date() + " " + halfDay.half());
  }
}
