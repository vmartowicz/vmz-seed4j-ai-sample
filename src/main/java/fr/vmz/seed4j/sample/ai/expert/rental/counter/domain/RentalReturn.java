package fr.vmz.seed4j.sample.ai.expert.rental.counter.domain;

import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RentalReturn(
  ContractId contract,
  Email customer,
  Category bookedCategory,
  BigDecimal dailyRate,
  int days,
  BigDecimal paid,
  LocalDateTime pickedUpAt,
  LocalDateTime plannedReturnAt,
  LocalDateTime returnedAt,
  RentalPoint pickUpPoint,
  RentalPoint returnPoint,
  int kilometres,
  BigDecimal missingFuel,
  boolean electric,
  boolean damaged
) {}
