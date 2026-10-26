package fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import java.time.LocalDate;
import java.time.Period;

public record Driver(String name, Email email, LocalDate birthDate, LocalDate licenceDate) {
  private static final int MINIMUM_AGE = 21;
  private static final int LICENCE_YEARS = 3;
  private static final int YOUNG_DRIVER_AGE = 25;

  public Driver {
    Assert.field("name", name).notBlank().maxLength(100);
    Assert.notNull("email", email);
    Assert.notNull("birthDate", birthDate);
    Assert.notNull("licenceDate", licenceDate);
  }

  public void assertCanDriveOn(LocalDate day) {
    if (ageOn(day) < MINIMUM_AGE) {
      throw new DriverNotEligibleException("Drivers must be at least 21 years old");
    }

    if (Period.between(licenceDate, day).getYears() < LICENCE_YEARS) {
      throw new DriverNotEligibleException("Drivers must have held a licence for 3 years");
    }
  }

  public boolean isYoungOn(LocalDate day) {
    return ageOn(day) < YOUNG_DRIVER_AGE;
  }

  private int ageOn(LocalDate day) {
    return Period.between(birthDate, day).getYears();
  }
}
