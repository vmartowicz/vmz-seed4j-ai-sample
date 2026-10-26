package fr.vmz.seed4j.sample.ai.expert.rental.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReturnReport(
  UUID contract,
  Email customer,
  Category bookedCategory,
  BigDecimal dailyRate,
  int bookedDays,
  BigDecimal paidRental,
  LocalDateTime pickedUpAt,
  LocalDateTime plannedReturnAt,
  LocalDateTime returnedAt,
  RentalPoint pickUpPoint,
  RentalPoint returnPoint,
  int kilometres,
  BigDecimal missingFuel,
  boolean electric,
  boolean damaged
) {
  public ReturnReport {
    Assert.notNull("contract", contract);
    Assert.notNull("customer", customer);
    Assert.notNull("bookedCategory", bookedCategory);
    Assert.field("kilometres", kilometres).min(0);
    Assert.field("missingFuel", missingFuel).min(0);
  }
}
