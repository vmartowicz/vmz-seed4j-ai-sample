package fr.vmz.seed4j.sample.ai.intermediate.parking.domain;

import java.time.YearMonth;

class MonthlyQuotaReachedException extends ParkingRuleViolationException {

  MonthlyQuotaReachedException(ResidentName resident, YearMonth month) {
    super(resident.get() + " already has 2 reservations in " + month);
  }
}
