package fr.vmz.seed4j.sample.ai.expert.rental.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

final class ReturnCharges {

  private static final long MINUTES_PER_DAY = 24 * 60;
  private static final long TOLERANCE_MINUTES = 59;
  private static final long HOURLY_LATE_FEE_UNTIL_MINUTES = 3 * 60;
  private static final BigDecimal LATE_FEE_PER_HOUR = new BigDecimal("15");
  private static final int INCLUDED_KILOMETRES_PER_DAY = 200;
  private static final BigDecimal PRICE_PER_EXTRA_KILOMETRE = new BigDecimal("0.25");
  private static final BigDecimal PRICE_PER_LITRE = new BigDecimal("2.50");
  private static final BigDecimal PRICE_PER_KWH = new BigDecimal("0.40");
  private static final BigDecimal ONE_WAY_FEE = new BigDecimal("50");
  private static final Set<RentalPoint> CITY_POINTS = Set.of(RentalPoint.PART_DIEU, RentalPoint.VILLEURBANNE);
  private static final int MAX_CREDITED_DAYS = 2;

  private ReturnCharges() {}

  static List<Charge> of(ReturnReport report) {
    var charges = new ArrayList<Charge>();
    var delay = Duration.between(report.plannedReturnAt(), report.returnedAt()).toMinutes();

    var lateDays = delay > HOURLY_LATE_FEE_UNTIL_MINUTES ? startedUnits(delay, MINUTES_PER_DAY) : 0;
    if (delay > TOLERANCE_MINUTES && lateDays == 0) {
      add(charges, ChargeKind.LATE_HOURS, LATE_FEE_PER_HOUR.multiply(BigDecimal.valueOf(startedUnits(delay, 60))));
    }
    add(charges, ChargeKind.LATE_DAYS, report.dailyRate().multiply(BigDecimal.valueOf(lateDays)));

    if (report.bookedCategory() != Category.PREMIUM) {
      var included = INCLUDED_KILOMETRES_PER_DAY * (report.bookedDays() + lateDays);
      add(charges, ChargeKind.EXTRA_KILOMETRES, PRICE_PER_EXTRA_KILOMETRE.multiply(BigDecimal.valueOf(Math.max(0, report.kilometres() - included))));
    }

    add(charges, ChargeKind.FUEL, report.missingFuel().multiply(report.electric() ? PRICE_PER_KWH : PRICE_PER_LITRE));

    if (isOneWay(report.pickUpPoint(), report.returnPoint())) {
      add(charges, ChargeKind.ONE_WAY, ONE_WAY_FEE);
    }

    var usedDays = startedUnits(Duration.between(report.pickedUpAt(), report.returnedAt()).toMinutes(), MINUTES_PER_DAY);
    var creditedDays = Math.min(MAX_CREDITED_DAYS, Math.max(0, report.bookedDays() - usedDays));
    add(charges, ChargeKind.EARLY_RETURN_CREDIT, report.dailyRate().multiply(BigDecimal.valueOf(creditedDays)).negate());

    return List.copyOf(charges);
  }

  private static boolean isOneWay(RentalPoint pickUp, RentalPoint returned) {
    return pickUp != returned && !(CITY_POINTS.contains(pickUp) && CITY_POINTS.contains(returned));
  }

  private static long startedUnits(long minutes, long unit) {
    return Math.max(1, (minutes + unit - 1) / unit);
  }

  private static void add(List<Charge> charges, ChargeKind kind, BigDecimal amount) {
    if (amount.signum() != 0) {
      charges.add(new Charge(kind, amount));
    }
  }
}
