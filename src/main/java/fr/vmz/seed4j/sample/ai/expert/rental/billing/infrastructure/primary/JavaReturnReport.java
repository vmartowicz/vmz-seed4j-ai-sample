package fr.vmz.seed4j.sample.ai.expert.rental.billing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.ReturnReport;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record JavaReturnReport(
  UUID contract,
  String customer,
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
  ReturnReport toDomain() {
    return new ReturnReport(
      contract,
      new Email(customer),
      bookedCategory,
      dailyRate,
      bookedDays,
      paidRental,
      pickedUpAt,
      plannedReturnAt,
      returnedAt,
      pickUpPoint,
      returnPoint,
      kilometres,
      missingFuel,
      electric,
      damaged
    );
  }
}
